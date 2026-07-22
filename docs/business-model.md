# Business Model: Independent Public-Sector Market-Entry & Procurement Compliance Service — Slovakia

## Classification

- Repository: `cloud-itonami-iso3166-svk`
- ISO 3166: `SVK` (Slovakia)
- Activity: public-procurement market-entry and ongoing regulatory-
  compliance navigation for an already-incorporated operator
- Social impact: [:eu-single-market-access :public-spend-transparency :cross-border-friction-reduction]

## Customer

- an already-incorporated `cloud-itonami-cofog-{code}` /
  `cloud-itonami-isco-{code}` / `cloud-itonami-unspsc-{segment}` /
  `cloud-itonami-{ISIC}` operator wanting to bid on a Slovak
  public contract
- a foreign SME or civic-tech vendor entering the public sector in
  Slovakia for the first time
- a `cloud-itonami-M6910` client that has just completed incorporation and
  now needs public-sector market access

## Offer

- registration walkthrough for the Úrad pre verejné obstarávanie's
  (ÚVO, Public Procurement Office) "Zoznam hospodárskych subjektov"
  (List of Economic Operators) -- ÚVO's own site describes this as an
  information system recording which economic operators have
  demonstrated eligibility to enter into public-procurement contracts
  or framework agreements (§ 187 ods. 7 zákona o verejnom obstarávaní)
  -- plus the actual EPVO (Elektronická platforma verejného
  obstarávania, isepvo.sk) economic-operator registration in IS EVO
  and/or Elektronické trhovisko, the two systems that carry out
  tendering itself (administered separately from ÚVO, by the Úrad
  podpredsedu vlády SR pre Plán obnovy a znalostnú ekonomiku)
- business/tax registration checklist: an entry in the Obchodný
  register (Business Register, orsr.sk), administered by the
  Ministerstvo spravodlivosti SR (Ministry of Justice) and kept by
  regional registry courts (registrové súdy); IČ DPH registration with
  Finančná správa once turnover exceeds the Zákon o DPH thresholds
  (50 000 EUR / 62 500 EUR)
- FOREIGN-INVESTMENT-SCREENING navigation: for engagements the
  government has designated "kritické zahraničné investície" (critical
  foreign investments, nariadenie vlády SR č. 61/2023 Z. z.), guiding
  the client through Ministerstvo hospodárstva SR's (MHSR) screening
  process under Zákon č. 497/2022 Z. z. (implementing EU Regulation
  2019/452) BEFORE any filing submission -- this is the genuinely
  EU-member-state-specific regulatory axis this blueprint's governor
  independently gates on (see GOVERNANCE.md / `marketentry.governor`)
- local-content / preferential-procurement navigation: EU-wide open
  tendering above EU thresholds (no national-content quota as an EU
  member state), but Slovak-language submission is typically required
- ongoing regulatory-change monitoring subscription
- compliance-audit export package for the client's own records

## Revenue

- per-engagement market-entry fee (one-time registration + checklist
  completion)
- recurring regulatory-change monitoring subscription
- compliance-audit export package

## Trust Controls

- any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off (`:filing/submit` is never automated at any phase)
- an engagement flagged as a "kritická zahraničná investícia" (critical
  foreign investment) that has NOT cleared Ministerstvo hospodárstva SR's
  (MHSR) screening under Zákon č. 497/2022 Z. z. is a HARD hold on
  `:filing/submit` that cannot be overridden by human approval alone --
  the governor's flagship check for this vertical
- a false or fabricated regulatory-requirement claim is a HARD hold that
  cannot be overridden by human approval alone — it must be corrected
  against a cited official source first
- this service does **not** provide legal or tax advice; characterization
  and filing on the client's behalf beyond checklist/draft assistance
  routes to Slovak-licensed counsel or a registered agent
- every requirement cites the official portal or regulation, never
  invented

## Boundary with adjacent actors (read before forking)

- **`com-etzhayyim-ooyake`** (etzhayyim/root): read-only civic-wayfinding
  mirror of government structure, non-commercial, barred from acting as
  or for the government (G3 impersonation ban). This blueprint is
  commercial and never claims to be an official channel.
- **`matsurigoto`** (etzhayyim/root): sovereign e-government statecraft —
  literally the government, for etzhayyim's own covenant or an adopting
  nation-state. This blueprint is an independent operator the government
  contracts with or that bids into its procurement — never the
  government.
- **`com-etzhayyim-toritsugi`** (etzhayyim/root): guides a consenting
  INDIVIDUAL citizen through their OWN procedure, non-profit,
  donation-only. This blueprint's client is a business operator, not an
  individual citizen, and it is commercial.
- **`legal-entity.etzhayyim.com`**: read-only aggregated company-registry
  data, no execution. This blueprint executes (gated) registrations.
- **`cloud-itonami-M6910`**: helps a client BECOME a legal entity
  (incorporation, ISIC 6910) — a prior, different regulatory phase
  (company law). This blueprint assumes incorporation is already done and
  handles public-procurement market entry (a different regulatory domain).
- **`cloud-itonami-cofog-{code}`**: a jurisdiction-agnostic operator
  template for ONE public function. This blueprint is the orthogonal
  jurisdiction-specific axis — the two compose (fork a COFOG-function
  blueprint AND this one to operate in Slovakia).
