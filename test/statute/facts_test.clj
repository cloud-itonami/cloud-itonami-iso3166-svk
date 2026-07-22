(ns statute.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest svk-has-spec-basis
  (let [sb (facts/spec-basis "SVK")]
    (is (= 3 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["SVK" "POL" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "POL"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["svk.zz-2018-18-ochrana-osobnych-udajov"]
         (mapv :statute/id (facts/by-topic "SVK" :privacy))))
  (is (= ["svk.zz-2001-311-zakonnik-prace"]
         (mapv :statute/id (facts/by-topic "SVK" :labor))))
  (is (empty? (facts/by-topic "ATL" :privacy))))
