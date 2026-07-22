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
  gate, not a market-access-documentation gate. This governor
  therefore has ONE flagship check (`fdi-screening-missing`), the same
  shape as Serbia's single-flagship precedent, deliberately: Slovakia's
  own required-evidence checklist (Zoznam hospodárskych subjektov / IČ
  DPH / DIČ) is ALREADY covered by the generic `evidence-incomplete`
  check every sibling actor in this fleet uses, so duplicating a
  second country-specific HARD check on top of that (the way Poland's
  NIP check sits alongside its EU-establishment check) would fabricate
  a distinction the FDI-screening research does not independently
  support. A smaller honest check set beats a padded one copied from a
  sibling.

  Six checks, in priority order, ALL HARD violations: a human approver
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
                                       true. FLAGSHIP genuinely new
                                       check for the iso3166 family
                                       (grep-verified absent as a
                                       governor check function name
                                       fleet-wide at build time).
                                       Grounded in Zákon č. 497/2022
                                       Z. z. o preverovaní zahraničných
                                       investícií (Ministerstvo
                                       hospodárstva Slovenskej
                                       republiky).
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5/6. Double-draft / double-submit prevention -- enforced off
                                       dedicated `:drafted?`/
                                       `:submitted?` facts (never a
                                       `:status` value)."
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
                           (engagement-fee-mismatch-violations request st)
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
