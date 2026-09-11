(ns statute.facts
  "General-law compliance catalog for Slovakia (SVK) -- per
  ADR-2607141700 (cloud-itonami-compliance-fact-federation), mirroring
  the shape of cloud-itonami-iso3166-pol/-srb/-jpn/-usa/-... et al.
  `statute.facts`. Slovakia was previously culture-catalog-only in
  this repo, with no statute.facts and no marketentry.facts -- this
  entry closes both structural gaps for the SVK entity in one wave.

  PROVENANCE (all three citations below fetched and read directly this
  session, PRIMARY OFFICIAL SOURCE, no Wayback fallback needed, no
  CAPTCHA/bot-detection challenge encountered):

  All three acts were confirmed via slov-lex.sk (the official
  Slovak legal-information portal / Zbierka zákonov -- its own static
  mirror's footer states verbatim 'Prevádzkovateľom služby je
  Ministerstvo spravodlivosti Slovenskej republiky', i.e. it is
  operated by the Ministry of Justice of the Slovak Republic,
  analogous in role to Poland's eli.gov.pl). For each act, this
  session fetched BOTH (a) the portal's own record page (confirming
  the exact official title verbatim via its <title>/JSON-LD Legislation
  metadata) AND (b) the full text of the ORIGINAL PROMULGATED VERSION
  ('vyhlásené znenie') via static.slov-lex.sk, reading each act's own
  final 'Účinnosť' (entry-into-force) provision directly:

  Obchodný zákonník (Commercial Code, 513/1991 Zb.) -- slov-lex.sk
  confirms the exact title verbatim: '513/1991 Zb. Obchodný
  zákonník'. The promulgated text's own § 775 states verbatim: 'Tento
  zákon nadobúda účinnosť 1. januárom 1992.' (This Act enters into
  effect on 1 January 1992), signed Havel/Dubček/Čalfa -- this is a
  pre-1993 federal Czechoslovak act ('Zb.' = Zbierka, the pre-1993
  collection, vs. 'Z. z.' = Zbierka zákonov, the post-1993 Slovak
  collection) inherited into Slovak law upon the 1993 dissolution, not
  a drafting error.

  Zákon o ochrane osobných údajov (Personal Data Protection Act,
  18/2018 Z. z.) -- slov-lex.sk confirms the exact title verbatim:
  '18/2018 Z. z. Zákon o ochrane osobných údajov a o zmene a doplnení
  niektorých zákonov'. The promulgated text's own Čl. XVI states
  verbatim: 'Tento zákon nadobúda účinnosť 25. mája 2018.' (This Act
  enters into effect on 25 May 2018 -- the same date GDPR itself
  became applicable), signed Kiska/Danko/Fico.

  Zákonník práce (Labour Code, 311/2001 Z. z.) -- slov-lex.sk confirms
  the exact title verbatim: '311/2001 Z. z. Zákonník práce'. The
  promulgated text's own § 256 states verbatim: 'Tento zákon nadobúda
  účinnosť 1. apríla 2002 okrem § 5 ods. 2 až 5 a § 241 až 250, ktoré
  nadobudnú účinnosť vstupom Slovenskej republiky do Európskej únie.'
  (This Act enters into effect on 1 April 2002, except §5(2)-(5) and
  §§241-250, which take effect upon the Slovak Republic's accession to
  the EU), signed Schuster/Migaš/Dzurinda.

  NOTE on `:statute/enacted-date` below: for Slovakia this field
  records the verified DATE OF ENTRY INTO FORCE ('nadobudnutie
  účinnosti', quoted directly from each act's own final-provisions
  article above), not necessarily the date of parliamentary passage --
  the two can differ (as they do for all three acts above). This is
  disclosed explicitly rather than presented as an enactment/passage
  date the way Poland's citations happen to be.

  An entry not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url/date.")

(def catalog
  "ISO3166 alpha-3 -> vector of statute entries."
  {"SVK"
   [{:statute/id "svk.zb-1991-513-obchodny-zakonnik"
     :statute/title "Obchodný zákonník (Commercial Code)"
     :statute/jurisdiction "SVK"
     :statute/kind :law
     :statute/law-number "513/1991 Zb."
     :statute/url "https://www.slov-lex.sk/pravne-predpisy/SK/ZZ/1991/513/"
     :statute/url-provenance :official-slov-lex-sk
     :statute/enacted-date "1992-01-01"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "svk.zz-2018-18-ochrana-osobnych-udajov"
     :statute/title "Zákon o ochrane osobných údajov (Personal Data Protection Act)"
     :statute/jurisdiction "SVK"
     :statute/kind :law
     :statute/law-number "18/2018 Z. z."
     :statute/url "https://www.slov-lex.sk/pravne-predpisy/SK/ZZ/2018/18/"
     :statute/url-provenance :official-slov-lex-sk
     :statute/enacted-date "2018-05-25"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:data-protection :privacy}}
    {:statute/id "svk.zz-2001-311-zakonnik-prace"
     :statute/title "Zákonník práce (Labour Code)"
     :statute/jurisdiction "SVK"
     :statute/kind :law
     :statute/law-number "311/2001 Z. z."
     :statute/url "https://www.slov-lex.sk/pravne-predpisy/SK/ZZ/2001/311/"
     :statute/url-provenance :official-slov-lex-sk
     :statute/enacted-date "2002-04-01"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :employment}}]})

(defn spec-basis [jurisdiction] (get catalog jurisdiction))

(defn coverage
  ([] (coverage (keys catalog)))
  ([jurisdictions]
   (let [have (filter catalog jurisdictions)
         missing (remove catalog jurisdictions)]
     {:requested (count jurisdictions)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-svk statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "SVK")) " Slovakia entries seeded "
                 "with slov-lex.sk (official Zbierka zákonov, Ministry of Justice) citations, "
                 "each cross-checked against the original promulgated text's own final-provisions article. "
                 "Extend `statute.facts/catalog`, never fabricate an id/url.")})))

(defn by-topic [jurisdiction topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis jurisdiction)))
