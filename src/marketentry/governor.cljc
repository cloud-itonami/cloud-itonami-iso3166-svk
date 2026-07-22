(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of jurisdictional procurement law, whether a foreign-investor
  engagement flagged as a 'kritická zahraničná investícia' has
  actually cleared Slovakia's FDI-screening process, whether a claimed
  engagement fee actually equals base + months x rate, or when a draft
  stops being a draft and becomes a real-world EPVO submission, so this
  MUST be a separate system able to *reject* a proposal and fall back
  to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints; this fleet's Poland
  implementation was the first *running* instance of this governor for
  the iso3166 family; this is Slovakia's).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual EPVO registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off'; 'a false or fabricated regulatory-requirement claim
  is a HARD hold') names exactly the checks below.

  UNLIKE Poland (whose flagship check is an EU-establishment/
  authorized-representative requirement flowing from EU procurement
  directives) and UNLIKE Serbia (an EU-CANDIDATE, non-EU-member state
  whose flagship check is a non-resident-PIB tax-ID requirement),
  Slovakia is an EU MEMBER STATE with its OWN distinct regulatory axis:
  a national FDI-SCREENING mechanism implementing EU Regulation
  2019/452. Independently WebFetch-verified this session against
  Ministerstvo hospodárstva Slovenskej republiky's (MHSR, Ministry of
  Economy) own site: Zákon č. 497/2022 Z. z. o preverovaní
  zahraničných investícií, effective 1. 3. 2023, requires SCREENING
  CLEARANCE for engagements the government has designated 'kritické
  zahraničné investície' (critical foreign investments, per nariadenie
  vlády SR č. 61/2023 Z. z.) -- a fundamentally different real-world
  fact from an establishment-presence check or a tax-ID-verification
  check: it is an INBOUND-INVESTMENT security/public-order screening
  gate, not a market-access-documentation gate.

  FOLLOW-UP CORRECTION PASS: a second, genuinely distinct structural
  fact was identified after the wave above landed -- Slovakia's
  e-procurement platform is itself split across two authorities the
  same way Latvia's EIS is (`cloud-itonami-iso3166-lva`'s IUB/VDAA
  pattern, reapplied here): ÚVO is the legal/regulatory-oversight
  authority, but IS EVO/EPVO (isepvo.sk) has been operated by the
  SEPARATE Úrad vlády Slovenskej republiky since 31 March 2022 -- ÚVO's
  own site explicitly disclaims operating it. This is not a copy for
  symmetry's sake: it is the SAME class of naive-source fusion mistake
  ('ÚVO operates IS EVO/EPVO') the LVA precedent exists to catch, now
  independently confirmed for Slovakia too, so this governor carries a
  SECOND flagship check (`platform-operator-fusion-violations`)
  alongside the FDI-screening one. Two field-verified HARD checks were
  also added for the required-evidence items this actor's
  `evidence-incomplete` check already names but did not, until now,
  independently re-verify per engagement: IČO (issued by Štatistický
  úrad SR, the Statistical Office -- NOT Finančná správa) and DIČ
  (issued by Finančná správa).

  Eight checks, in priority order, ALL HARD violations: a human approver
  CANNOT override them. The confidence/actuation gate is SOFT: it asks
  a human to look (low confidence / actuation), and the human may
  approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. FDI-screening missing       -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-fdi-screening? true`,
                                       INDEPENDENTLY verify
                                       `:fdi-screening-cleared?` is
                                       true. FLAGSHIP #1. Grounded in
                                       Zákon č. 497/2022 Z. z. o
                                       preverovaní zahraničných
                                       investícií (Ministerstvo
                                       hospodárstva Slovenskej
                                       republiky).
    4. Platform-operator fusion    -- for `:jurisdiction/assess`, when
                                       the jurisdiction has a distinct
                                       platform-operator spec-basis on
                                       file, INDEPENDENTLY verify the
                                       proposal keeps ÚVO (legal/
                                       regulatory-oversight authority)
                                       and Úrad vlády Slovenskej
                                       republiky (IS EVO/EPVO's
                                       separate operator since 31 March
                                       2022) DISTINCT -- never
                                       collapses them into one fused
                                       'ÚVO operates IS EVO/EPVO' fact.
                                       FLAGSHIP #2, mirroring
                                       `cloud-itonami-iso3166-lva`'s
                                       IUB/VDAA check.
    5. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    6. IČO unverified              -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-ico? true`,
                                       INDEPENDENTLY check
                                       `:ico-verified?`. Grounded in
                                       Štatistický úrad SR (Statistical
                                       Office) -- NOT Finančná správa.
    7. DIČ unverified              -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-dic? true`,
                                       INDEPENDENTLY check
                                       `:dic-verified?`. Grounded in
                                       Finančná správa DIČ assignment.
    8. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real EPVO package and submitting a real EPVO registration
  are the two real-world actuation events this actor performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(Obchodný register/Zoznam hospodárskych subjektov/EPVO登録/IČ DPH・DIČ等)が充足していない状態での提案"}]))))

(defn- fdi-screening-missing-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-fdi-screening? true`, INDEPENDENTLY verify
  `:fdi-screening-cleared?` is true -- the flagship genuinely new
  check this vertical adds. CONDITIONAL on the engagement's own
  `:requires-fdi-screening?` ground truth (most public-procurement
  engagements do NOT trigger this -- FDI screening under Zákon č.
  497/2022 Z. z. applies only to engagements the government has
  designated 'kritické zahraničné investície' per nariadenie vlády SR
  č. 61/2023 Z. z., a narrower set than all foreign-operator
  engagements). Grounded in Ministerstvo hospodárstva Slovenskej
  republiky (MHSR)'s own confirmation that the screening package took
  effect 1. 3. 2023, implementing nariadenie (EÚ) 2019/452."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-fdi-screening? e))
                 (not (true? (:fdi-screening-cleared? e))))
        [{:rule :fdi-screening-missing
          :detail (str subject " は preverovanie zahraničných investícií (zákon č. 497/2022 Z. z.) を要するが MHSR 承認が未確認 -- 提出提案は進められない")}]))))

(defn- platform-operator-fusion-violations
  "For `:jurisdiction/assess`, when the jurisdiction has a distinct
  platform-operator spec-basis on file (`marketentry.facts/
  platform-operator-spec-basis`), INDEPENDENTLY verify the proposal's
  own claim keeps the legal/regulatory-oversight authority and the
  e-procurement platform's separate technical operator DISTINCT.
  FLAGSHIP #2 -- mirrors `cloud-itonami-iso3166-lva`'s
  `platform-operator-fusion-violations` (IUB/VDAA), reapplied here to
  Slovakia's OWN independently-confirmed regulator/operator split: ÚVO
  (Úrad pre verejné obstarávanie) is the legal/oversight authority; IS
  EVO/EPVO (isepvo.sk) has been operated by the SEPARATE Úrad vlády
  Slovenskej republiky since 31 March 2022 -- ÚVO's own site explicitly
  disclaims operating it. A proposal that collapses them into a single
  fused authority (the near-universal naive-source mistake for this
  jurisdiction: 'ÚVO operates IS EVO/EPVO'), or omits one, or cites
  either against the wrong catalogued value, is a HARD violation."
  [{:keys [op]} proposal]
  (when (= op :jurisdiction/assess)
    (let [value (:value proposal)
          iso3 (:jurisdiction value)
          pob (facts/platform-operator-spec-basis iso3)]
      (when pob
        (let [legal (:platform-legal-authority value)
              operator (:platform-technical-operator value)]
          (when (or (nil? legal)
                    (nil? operator)
                    (= legal operator)
                    (not= legal (:platform-oversight-authority pob))
                    (not= operator (:platform-operator-authority pob)))
            [{:rule :platform-operator-fused
              :detail (str iso3 " のEPVO/IS EVOプラットフォーム運営主体の記載がÚVO(法的監督機関)と実運営者(Úrad vlády SR)を"
                          "混同しているか、未記載/カタログ値と不一致 -- 別個の主体として検証できない")}]))))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- ico-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-ico? true`, INDEPENDENTLY check `:ico-verified?` --
  grounded in Štatistický úrad SR (Statistical Office), the body that
  issues IČO -- a DIFFERENT authority from Finančná správa (which
  issues DIČ/IČ DPH, see `dic-unverified-violations`). CONDITIONAL on
  the engagement's own ground truth."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-ico? e))
                 (not (true? (:ico-verified? e))))
        [{:rule :ico-unverified
          :detail (str subject " はŠtatistický úrad SR発行のIČO確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- dic-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-dic? true`, INDEPENDENTLY check `:dic-verified?` --
  grounded in Finančná správa DIČ assignment. CONDITIONAL on the
  engagement's own ground truth."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-dic? e))
                 (not (true? (:dic-verified? e))))
        [{:rule :dic-unverified
          :detail (str subject " はFinančná správa発行のDIČ確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (fdi-screening-missing-violations request st)
                           (platform-operator-fusion-violations request proposal)
                           (engagement-fee-mismatch-violations request st)
                           (ico-unverified-violations request st)
                           (dic-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
