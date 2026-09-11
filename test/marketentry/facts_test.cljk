(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest svk-has-spec-basis
  (let [sb (facts/spec-basis "SVK")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/fdi-screening-spec-basis "SVK")))
    (is (some? (facts/platform-operator-spec-basis "SVK")))
    (is (some? (facts/corporate-number-spec-basis "SVK")))
    (is (some? (facts/dic-spec-basis "SVK")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "SVK")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "SVK" all)))
    (is (not (facts/required-evidence-satisfied? "SVK" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["SVK" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))

;; ---- the central fabrication trap for this jurisdiction: ÚVO vs Úrad vlády SR ----

(deftest uvo-and-urad-vlady-sr-are-distinct-authorities
  (testing "IS EVO/EPVO platform-operator spec-basis keeps legal oversight (ÚVO) and the operator (Úrad vlády SR) SEPARATE"
    (let [pob (facts/platform-operator-spec-basis "SVK")]
      (is (some? pob))
      (is (some? (:platform-oversight-authority pob)))
      (is (some? (:platform-operator-authority pob)))
      (is (not= (:platform-oversight-authority pob) (:platform-operator-authority pob))
          "ÚVO (oversight) and Úrad vlády SR (operator) must never be the same value")
      (is (re-find #"ÚVO" (:platform-oversight-authority pob)))
      (is (re-find #"Úrad vlády" (:platform-operator-authority pob)))
      (is (not (re-find #"ÚVO" (:platform-operator-authority pob)))
          "the operator value itself must not also name ÚVO (no fusion)")
      (is (re-find #"31 March 2022" (:platform-operator-note pob))
          "the note must record the 31 March 2022 transfer away from ÚVO"))))

(deftest owner-authority-is-uvo-not-fused-with-platform-operator
  (testing "top-level :owner-authority (the general procurement-law authority) is ÚVO, distinct from the platform operator"
    (let [sb (facts/spec-basis "SVK")]
      (is (re-find #"ÚVO" (:owner-authority sb)))
      (is (not= (:owner-authority sb) (:platform-operator-authority (facts/platform-operator-spec-basis "SVK")))))))

(deftest no-platform-operator-spec-basis-for-jurisdictions-without-one
  (is (nil? (facts/platform-operator-spec-basis "USA")))
  (is (nil? (facts/platform-operator-spec-basis "ATL"))))

;; ---- the other fabrication trap for this jurisdiction: IČO vs DIČ ----

(deftest ico-is-statistical-office-not-financna-sprava
  (testing "IČO's owner authority is the Statistical Office, never Finančná správa"
    (let [cnb (facts/corporate-number-spec-basis "SVK")]
      (is (some? cnb))
      (is (re-find #"Štatistický úrad" (:corporate-number-owner-authority cnb)))
      (is (not (re-find #"Finančná správa" (:corporate-number-owner-authority cnb))))
      (is (re-find #"Do not attribute IČO issuance to Finančná správa" (:corporate-number-legal-basis cnb))
          "the legal-basis text itself must disclaim Finančná správa"))))

(deftest dic-is-financna-sprava-distinct-from-ico
  (testing "DIČ's owner authority is Finančná správa, distinct from the IČO/Statistical-Office authority"
    (let [dic (facts/dic-spec-basis "SVK")
          cnb (facts/corporate-number-spec-basis "SVK")]
      (is (some? dic))
      (is (re-find #"Finančná správa" (:dic-owner-authority dic)))
      (is (not= (:dic-owner-authority dic) (:corporate-number-owner-authority cnb))))))
