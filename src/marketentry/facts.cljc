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
          :fdi-screening-provenance "https://www.economy.gov.sk/podnikatelske-prostredie/preverovanie-zahranicnych-investicii/zakladne-informacie"}
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
