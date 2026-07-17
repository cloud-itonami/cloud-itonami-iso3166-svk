(ns culture.facts
  "Country-level regional-culture catalog for Slovakia (SVK) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"SVK"
   [{:culture/id "svk.dish.bryndzove-halusky"
     :culture/name "Bryndzové halušky"
     :culture/country "SVK"
     :culture/kind :dish
     :culture/summary "One of the national dishes of Slovakia, consisting of boiled potato dough dumplings topped with soft sheep cheese (bryndza) and typically bacon bits."
     :culture/url "https://en.wikipedia.org/wiki/Bryndzov%C3%A9_halu%C5%A1ky"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.dish.kapustnica"
     :culture/name "Kapustnica"
     :culture/country "SVK"
     :culture/kind :dish
     :culture/summary "Slovak soup made from sauerkraut and sausage, traditionally eaten around Christmas."
     :culture/url "https://en.wikipedia.org/wiki/Slovak_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.dish.strapacky"
     :culture/name "Strapačky"
     :culture/country "SVK"
     :culture/kind :dish
     :culture/summary "Dish popular in Slovakia (and Hungary) combining potato dumplings (halušky) with stewed sauerkraut instead of cheese."
     :culture/url "https://en.wikipedia.org/wiki/Strapa%C4%8Dky"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.product.bryndza"
     :culture/name "Bryndza"
     :culture/country "SVK"
     :culture/kind :product
     :culture/summary "Sheep milk cheese from Central and Eastern Europe; the Slovak variety 'Slovenská bryndza' holds Protected Geographical Indication (PGI) status."
     :culture/url "https://en.wikipedia.org/wiki/Bryndza"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.beverage.slivovica"
     :culture/name "Slivovica"
     :culture/country "SVK"
     :culture/kind :beverage
     :culture/summary "Traditional plum brandy produced across Central and Southeastern Europe; known in Slovakia as slivovica, with strong cultural presence in local traditions."
     :culture/url "https://en.wikipedia.org/wiki/Slivovitz"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.craft.fujara"
     :culture/name "Fujara"
     :culture/country "SVK"
     :culture/kind :craft
     :culture/summary "Large Slovak folk wind instrument; 'The Fujara and its Music' was inscribed on UNESCO's Representative List of the Intangible Cultural Heritage of Humanity in 2008."
     :culture/url "https://en.wikipedia.org/wiki/Fujara"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.festival.fasiangy"
     :culture/name "Fašiangy"
     :culture/country "SVK"
     :culture/kind :festival
     :culture/summary "Slovak name for the Slavic pre-Lenten carnival tradition, observed before Great Lent."
     :culture/url "https://en.wikipedia.org/wiki/Slavic_Carnival"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.heritage.vlkolinec"
     :culture/name "Vlkolínec"
     :culture/country "SVK"
     :culture/kind :heritage
     :culture/summary "Traditional Slovak mountain settlement, listed as a UNESCO World Heritage Site since 1993."
     :culture/url "https://en.wikipedia.org/wiki/Vlkol%C3%ADnec"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "svk.heritage.banska-stiavnica"
     :culture/name "Banská Štiavnica"
     :culture/country "SVK"
     :culture/kind :heritage
     :culture/summary "Historic Slovak mining town and its surroundings, proclaimed a UNESCO World Heritage Site on 11 December 1993."
     :culture/url "https://en.wikipedia.org/wiki/Bansk%C3%A1_%C5%A0tiavnica"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-svk culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "SVK"))
                 " SVK entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
