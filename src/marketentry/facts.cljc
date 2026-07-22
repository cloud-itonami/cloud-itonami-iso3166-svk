(ns marketentry.facts
  "Slovakia (Slovak Republic, EU member state) market-entry catalog.
  Every SVK field below is curl/WebFetch-verified this session
  directly against the official government sites named in
  `:provenance` -- an entry NOT in `catalog` has no spec-basis, full
  stop; extend `catalog`, never invent an authority/URL.

  Verified this session (all official .gov.sk / government-operated
  sites; none required a Wayback Machine fallback and none presented a
  CAPTCHA/bot-detection challenge):

  - https://www.uvo.gov.sk/ -- Úrad pre verejné obstarávanie (ÚVO,
    Public Procurement Office); the page's own <title> reads verbatim
    'Úvod - ÚVO'. Administers Zákon č. 343/2015 Z. z. o verejnom
    obstarávaní (slov-lex.sk's own <title>/JSON-LD confirms the exact
    name verbatim: '343/2015 Z. z. Zákon o verejnom obstarávaní a o
    zmene a doplnení niektorých zákonov'). ÚVO's own
    'Zoznam hospodárskych subjektov' (List of Economic Operators) page
    states verbatim: 'Informačný systém úradu, obsahujúci údaje o
    fyzických a právnických osobách -- hospodárskych subjektoch,
    ktorí preukázali spôsobilosť na uzatváranie zmlúv alebo rámcových
    dohôd vo verejnom obstarávaní... V súlade s § 187 ods. 7 zákona o
    verejnom obstarávaní...' -- a real ÚVO information system keyed to
    a specific statute article, not a guessed registry name.
  - https://www.isepvo.sk/ -- 'Elektronická platforma verejného
    obstarávania' (EPVO). GENUINE INSTITUTIONAL SPLIT, unlike Poland's
    single e-Zamówienia portal run entirely by UZP: ÚVO's own site
    states (verbatim, Slovak) that IS EVO 'prechádza k 31.03.2022 do
    správy Úradu vlády Slovenskej republiky' (passes, as of 31 March
    2022, into the administration of the Office of the Government of
    the Slovak Republic). isepvo.sk's own current homepage (fetched
    this session) identifies itself as the official site of the 'Úrad
    podpredsedu vlády Slovenskej republiky pre Plán obnovy a znalostnú
    ekonomiku' (Office of the Deputy PM for the Recovery Plan and
    Knowledge Economy) and states verbatim: 'Funkcionality
    elektronickej platformy sú v súčasnosti zabezpečené s využitím
    elektronického trhoviska (ET) a informačného systému Elektronického
    verejného obstarávania (IS EVO).' -- i.e. TWO systems: IS EVO
    (above-threshold + most below-threshold contracts, § 108/110-113)
    and Elektronické trhovisko (ET, simplified below-threshold
    procurement of commonly-available goods/services, § 109), both
    administered OUTSIDE ÚVO. ÚVO itself retains the supervisory/
    appeals authority (dohľad, námietky) per its own site's own nav
    structure. This asymmetry with Poland is deliberate and disclosed,
    not an omission.
  - https://www.orsr.sk/ -- Obchodný register SR na Internete
    (Business Register); its own homepage banner states verbatim
    'MINISTERSTVO SPRAVODLIVOSTI SLOVENSKEJ REPUBLIKY' and links
    directly to justice.gov.sk. Its 'O obchodnom registri SR' page
    states verbatim: '...register vedie okresný súd v sídle krajského
    súdu... Súd, ktorý vedie obchodný register sa označuje ako
    \"registrový súd\".' (registry court). Administered under Zákon č.
    530/2003 Z. z. o obchodnom registri (slov-lex.sk confirms this
    exact title verbatim).
  - https://www.economy.gov.sk/podnikatelske-prostredie/preverovanie-
    zahranicnych-investicii/zakladne-informacie -- Ministerstvo
    hospodárstva Slovenskej republiky (MHSR, Ministry of Economy)'s own
    page states verbatim: 'Legislatívny balíček upravujúci preverovanie
    zahraničných investícií nadobudol účinnosť 01. 03. 2023. Jeho
    základom je zákon č. 497/2022 Z. z. o preverovaní zahraničných
    investícií... na základe nariadenia Európskeho Parlamentu a Rady
    (EÚ) 2019/452...' (slov-lex.sk independently confirms the exact
    act title). The Ministry's own 'Legislatíva' subpage (fetched this
    session) additionally names the two implementing instruments:
    Nariadenie vlády SR č. 61/2023 Z. z. (defines 'kritické zahraničné
    investície' -- critical foreign investments, i.e. the screened
    sectors) and Vyhláška MH SR č. 64/2023 Z. z. (the application-form
    decree). FLAGSHIP check basis -- see `marketentry.governor`
    `fdi-screening-missing-violations`. GENUINELY DIFFERENT axis from
    Poland's EU-establishment/NIP checks or Serbia's non-resident-PIB
    check: Slovakia is an EU MEMBER STATE (unlike Serbia) implementing
    EU Regulation 2019/452's INBOUND foreign-direct-investment
    screening mechanism -- a fundamentally different regulatory concept
    from an establishment-freedom or a tax-ID-verification check.
  - https://www.financnasprava.sk/ -- Finančná správa Slovenskej
    republiky (Financial Administration). Its own VAT-registration page
    (/sk/podnikatelia/dane/dan-z-pridanej-hodnoty/registracna-povinnost-
    pre-dph) states verbatim the mandatory-registration thresholds
    under Zákon o DPH (č. 222/2004 Z. z., slov-lex.sk confirms the
    title verbatim): registration as a VAT payer is required once
    turnover exceeds 50 000 eur in the preceding calendar year
    (§ 4 ods. 1) or 62 500 eur within the current calendar year (same
    page, § 4 ods. 1 second limb). General tax registration / DIČ
    assignment is governed by Zákon č. 563/2009 Z. z. o správe daní
    (daňový poriadok) (slov-lex.sk confirms this exact title verbatim).
    DISCLOSED GAP: this session confirmed the Tax Procedure Act's exact
    title and administering agency (Finančná správa) but did NOT
    independently pull the specific DIČ-assignment article number --
    financnasprava.sk's general registration-index page is a
    client-rendered SPA shell with no server-rendered body text this
    session (unlike its VAT-registration page, which does render
    server-side). Do not treat a DIČ article-level citation as verified
    until that specific gap is closed.

  FOLLOW-UP CORRECTION PASS (added after the wave above landed): the
  wave above's :required-evidence already lists an EPVO/IS EVO
  registration item and correctly disclaims ÚVO as its operator in
  :national-spec's own text, but it did not yet turn that split into
  its own governor-checked field pair, and did not yet carry an IČO
  citation at all. This pass adds three field groups without touching
  anything already verified above:

  - `:platform-oversight-authority`/`:platform-operator-authority`/
    `:platform-operator-note`/`:platform-operator-provenance` --
    SECOND flagship field group, the SAME regulator/operator-split
    pattern `cloud-itonami-iso3166-lva` uses for IUB/VDAA (EIS),
    reapplied here: ÚVO (Úrad pre verejné obstarávanie) is the legal/
    regulatory-oversight authority; IS EVO/EPVO (isepvo.sk) is
    operated by a SEPARATE authority -- as of 31 March 2022 its
    management passed FROM ÚVO TO Úrad vlády Slovenskej republiky
    (Office of the Government of the Slovak Republic), operationally
    via the Úrad podpredsedu vlády SR pre Plán obnovy a znalostnú
    ekonomiku (Office of the Deputy PM for the Recovery Plan and
    Knowledge Economy) -- consistent with, and more granular than, the
    wave above's own :national-spec text. ÚVO's own site explicitly
    disclaims operating it; the near-universal naive-source mistake is
    'ÚVO operates IS EVO/EPVO'. `marketentry.governor`'s
    `platform-operator-fusion-violations` HARD-holds any
    `:jurisdiction/assess` proposal that collapses these into one
    fused fact. A third system, EKS (eks.sk), is a separate
    below-threshold e-marketplace, structurally distinct from EPVO --
    this catalog does NOT assert a specific current operator for EKS
    (not independently confirmed; existence noted here only).
  - `:corporate-number-owner-authority`/`:corporate-number-legal-basis`/
    `:corporate-number-provenance` -- IČO (8-digit business/
    organisation identification number), issued by Štatistický úrad
    Slovenskej republiky (Statistical Office of the Slovak Republic) --
    a body DISTINCT from Finančná správa (which issues DIČ/IČ DPH, see
    below). Do not attribute IČO issuance to Finančná správa.
  - `:dic-owner-authority`/`:dic-legal-basis`/`:dic-provenance` -- DIČ
    (tax identification number) assignment under Zákon č. 563/2009
    Z. z. o správe daní (daňový poriadok), administered by Finančná
    správa via the local daňový úrad -- the SAME disclosed-gap caveat
    above (no article-level citation pulled) still applies to this
    field group; only the act title and administering agency are
    treated as verified.

  An entry not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url/date.")

(def catalog
  {"SVK" {:name "Slovakia"
          :owner-authority "Úrad pre verejné obstarávanie (ÚVO, Public Procurement Office)"
          :legal-basis "Zákon č. 343/2015 Z. z. o verejnom obstarávaní a o zmene a doplnení niektorých zákonov"
          :national-spec "EPVO (Elektronická platforma verejného obstarávania, isepvo.sk): IS EVO (above-threshold + most below-threshold contracts, § 108/110-113) + Elektronické trhovisko (below-threshold marketplace for commonly-available goods/services, § 109); administered by the Úrad podpredsedu vlády SR pre Plán obnovy a znalostnú ekonomiku, NOT by ÚVO itself (ÚVO retains supervisory/appeals authority)"
          :provenance "https://www.uvo.gov.sk/"
          :required-evidence ["Obchodný register extract (výpis z Obchodného registra SR)"
                              "Zoznam hospodárskych subjektov registration record (ÚVO, § 187 ods. 7 zákona o verejnom obstarávaní)"
                              "EPVO (IS EVO / Elektronické trhovisko) economic-operator registration record"
                              "IČ DPH / DIČ record (Finančná správa)"
                              "Authorized-signatory record"]
          ;; Flagship check basis -- see marketentry.governor
          ;; `fdi-screening-missing-violations`. Grounded in Zákon č.
          ;; 497/2022 Z. z., effective 1. 3. 2023, implementing EU
          ;; Regulation 2019/452; screened sectors ('kritické
          ;; zahraničné investície') fixed by nariadenie vlády SR č.
          ;; 61/2023 Z. z.
          :fdi-screening-owner-authority "Ministerstvo hospodárstva Slovenskej republiky (MHSR, Ministry of Economy)"
          :fdi-screening-legal-basis "Zákon č. 497/2022 Z. z. o preverovaní zahraničných investícií a o zmene a doplnení niektorých zákonov (účinnosť 1. 3. 2023), implementujúci nariadenie Európskeho parlamentu a Rady (EÚ) 2019/452; kritické zahraničné investície ustanovuje nariadenie vlády SR č. 61/2023 Z. z., formulár žiadosti vyhláška MH SR č. 64/2023 Z. z."
          :fdi-screening-provenance "https://www.economy.gov.sk/podnikatelske-prostredie/preverovanie-zahranicnych-investicii/zakladne-informacie"
          ;; SECOND flagship field group -- see namespace docstring
          ;; "FOLLOW-UP CORRECTION PASS". Mirrors cloud-itonami-iso3166-lva's
          ;; IUB/VDAA (EIS) split.
          :platform-oversight-authority "Úrad pre verejné obstarávanie (ÚVO, Public Procurement Office)"
          :platform-operator-authority "Úrad vlády Slovenskej republiky (Office of the Government of the Slovak Republic) -- since 31 March 2022, operationally via the Úrad podpredsedu vlády SR pre Plán obnovy a znalostnú ekonomiku (Office of the Deputy PM for the Recovery Plan and Knowledge Economy)"
          :platform-operator-note "IS EVO / EPVO (Elektronická platforma verejného obstarávania, isepvo.sk -- above-threshold + most below-threshold contracts, previously at evo.gov.sk) is NOT operated by ÚVO. As of 31 March 2022, its management transferred FROM ÚVO TO Úrad vlády Slovenskej republiky; ÚVO's own site explicitly states it no longer operates this system, retaining only the supervisory/appeals (dohľad, námietky) authority and its own separate eForms/IS ÚVO notice-publication system. A third system, EKS (eks.sk), is a separate below-threshold e-marketplace structurally distinct from EPVO -- no specific current operator asserted for EKS here (not independently confirmed). Do not fuse ÚVO and the IS EVO/EPVO operator into one fact."
          :platform-operator-provenance "https://www.uvo.gov.sk/"
          :corporate-number-owner-authority "Štatistický úrad Slovenskej republiky (Statistical Office of the Slovak Republic)"
          :corporate-number-legal-basis "IČO (8-digit business/organisation identification number) is issued by the Statistical Office -- a body DISTINCT from Finančná správa (which issues DIČ and IČ DPH, see :dic-legal-basis). Do not attribute IČO issuance to Finančná správa."
          :corporate-number-provenance "https://www.statistics.sk/"
          :dic-owner-authority "Finančná správa Slovenskej republiky (Financial Administration), via the local daňový úrad (tax office)"
          :dic-legal-basis "DIČ (tax identification number) assignment under Zákon č. 563/2009 Z. z. o správe daní (daňový poriadok) -- general tax registration, administered by Finančná správa; distinct from IČ DPH (VAT registration under Zákon č. 222/2004 Z. z., mandatory once turnover exceeds 50 000 EUR/62 500 EUR) and from IČO (Statistical Office, NOT Finančná správa). Article-level citation not independently pulled this session -- see namespace docstring disclosed gap."
          :dic-provenance "https://www.financnasprava.sk/"}
   "USA" {:name "United States" :owner-authority "GSA/SAM.gov" :legal-basis "FAR"
          :national-spec "SAM.gov" :provenance "https://sam.gov/"
          :required-evidence ["EIN record" "SAM.gov registration record" "State business registration record" "SAM UEI verification record"]}
   "DEU" {:name "Germany" :owner-authority "e-Vergabe" :legal-basis "GWB/VgV"
          :national-spec "e-Vergabe" :provenance "https://www.evergabe-online.de/"
          :required-evidence ["Handelsregister extract" "e-Vergabe registration record" "USt-IdNr record" "Authorized-representative record"]}
   "NLD" {:name "Netherlands" :owner-authority "TenderNed" :legal-basis "Aanbestedingswet"
          :national-spec "TenderNed" :provenance "https://www.tenderned.nl/"
          :required-evidence ["KvK extract" "TenderNed registration" "BTW record" "Authorized-representative record"]}})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s) missing (remove catalog iso3s)]
     {:requested (count iso3s) :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note "R0 catalog seed"})))

(defn required-evidence-satisfied? [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (= (count required-evidence) (count (filter (set submitted) required-evidence)))))

(defn evidence-checklist [iso3] (:required-evidence (spec-basis iso3) []))

(defn fdi-screening-spec-basis
  "Spec-basis for the flagship `fdi-screening-missing` governor check
  -- Slovakia's EU-Regulation-2019/452-implementing FDI-screening
  mechanism, Zákon č. 497/2022 Z. z."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:fdi-screening-owner-authority sb)
      (select-keys sb [:fdi-screening-owner-authority :fdi-screening-legal-basis :fdi-screening-provenance]))))

(defn platform-operator-spec-basis
  "The jurisdiction's e-procurement PLATFORM-OPERATOR citation, or
  nil -- the SECOND flagship field group for this vertical (see
  namespace docstring), mirroring `cloud-itonami-iso3166-lva`'s
  IUB/VDAA split. Keeps the legal/regulatory-oversight authority
  (`:platform-oversight-authority`) and the platform's separate
  technical operator (`:platform-operator-authority`) as two DISTINCT
  values so a consumer (governor, advisor, UI) is structurally
  prevented from collapsing them into one fused fact. For SVK: ÚVO is
  the oversight authority, Úrad vlády Slovenskej republiky is IS
  EVO/EPVO's operator since 31 March 2022."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:platform-operator-authority sb)
      (select-keys sb [:platform-oversight-authority
                       :platform-operator-authority
                       :platform-operator-note
                       :platform-operator-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / statistical-ID regime, or
  nil. For SVK this is IČO, issued by Štatistický úrad SR (Statistical
  Office) -- a DIFFERENT authority from Finančná správa (see
  `dic-spec-basis`)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn dic-spec-basis
  "The jurisdiction's tax-ID (DIČ) regime, or nil. For SVK this is
  Finančná správa's DIČ assignment under Zákon č. 563/2009 Z. z. --
  DISTINCT from IČO (Statistical Office, see `corporate-number-spec-basis`)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:dic-owner-authority sb)
      (select-keys sb [:dic-owner-authority :dic-legal-basis :dic-provenance]))))
