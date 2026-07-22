# cloud-itonami-iso3166-svk

Open ISO 3166 Blueprint for **SVK**: Slovak Republic (EU member state).

**`:implemented`** for **SVK**. Flagship `fdi-screening-missing`
(Zákon č. 497/2022 Z. z. o preverovaní zahraničných investícií).

```
clojure -M:dev:test
```

## Official surface (web-verified)

- Procurement: Úrad pre verejné obstarávanie (ÚVO, uvo.gov.sk) administers
  Zákon č. 343/2015 Z. z. o verejnom obstarávaní and the "Zoznam
  hospodárskych subjektov" (List of Economic Operators, § 187 ods. 7).
  The actual e-procurement platform, EPVO (isepvo.sk: IS EVO +
  Elektronické trhovisko), is administered separately -- by the Úrad
  podpredsedu vlády SR pre Plán obnovy a znalostnú ekonomiku, not by
  ÚVO itself (ÚVO retains supervisory/appeals authority). This
  institutional split is genuine and disclosed, not an omission.
- Business/tax: Obchodný register (orsr.sk), administered by the
  Ministerstvo spravodlivosti SR (Ministry of Justice) via regional
  registry courts, under Zákon č. 530/2003 Z. z. o obchodnom registri.
  Finančná správa administers IČ DPH registration (Zákon č. 222/2004
  Z. z., mandatory once turnover exceeds 50 000 EUR/62 500 EUR) and
  general tax registration (Daňový poriadok, Zákon č. 563/2009 Z. z.).
- Foreign investment: Slovakia, as an EU member state, implements EU
  Regulation 2019/452's inbound-FDI-screening mechanism via Zákon č.
  497/2022 Z. z. o preverovaní zahraničných investícií (effective
  1. 3. 2023), administered by Ministerstvo hospodárstva SR (MHSR).
  Screened sectors ("kritické zahraničné investície") are fixed by
  nariadenie vlády SR č. 61/2023 Z. z. This is a GENUINELY DIFFERENT
  regulatory axis from Poland's EU-establishment/NIP checks -- an
  inbound-investment security/public-order gate, not a market-access-
  documentation gate -- and is this vertical's flagship governor check.

See `src/marketentry/facts.cljc` and `src/statute/facts.cljc` for the
full citation trail (including one disclosed gap: this session did not
independently pull the specific DIČ tax-ID-assignment article number).

This repository designs a forkable OSS business for an independent
public-sector market-entry consultant: an already-incorporated operator
(e.g. a `cloud-itonami-cofog-{code}`, `cloud-itonami-isco-{code}`,
`cloud-itonami-unspsc-{segment}` or `cloud-itonami-{ISIC}` blueprint
fork) gets a Compliance Advisor + independent **Market-Entry Compliance
Governor** to navigate public-procurement registration, local business/
tax registration, and EU single-market rules in Slovakia, so the
operator can win and service a government contract without hiring a
full in-house compliance department.

## No robotics premise — digital/data service exemption

Market-entry and procurement-compliance navigation is a pure data/software
service with no physical-domain work (portal registration, document
checklists, regulatory-change monitoring) — the same exemption class as
`cloud-itonami-6310` (HR SaaS replacement) and `cloud-itonami-gtin-*`.
`blueprint.edn` sets `:itonami.blueprint/robotics false` and
`:required-technologies` lists only real capabilities (`:identity`,
`:forms`, `:dmn`, `:bpmn`, `:audit-ledger`), no `:robotics`.

## Core Contract

```text
operator intake + prior filing history
        |
        v
Compliance Advisor -> Market-Entry Compliance Governor -> filing draft, or human sign-off
        |
        v
gated portal registration / filing submission + audit ledger
```

No automated proposal can submit a portal registration or filing the
governor refuses, suppress a compliance record, or claim a legal/tax
conclusion the governor has not cleared. `:filing/submit` is never in any
phase's `:auto` set — it always requires human sign-off (mirrors
`cloud-itonami-M6910`'s `filing-submit-never-auto-at-any-phase`
invariant).

## What this is NOT

- **Not the government of Slovakia.** See
  [`docs/business-model.md`](docs/business-model.md) for the boundary with
  `com-etzhayyim-ooyake` (read-only civic mirror), `matsurigoto` (sovereign
  statecraft), `com-etzhayyim-toritsugi` (individual citizen concierge),
  `legal-entity.etzhayyim.com` (read-only data aggregation), and
  `cloud-itonami-M6910` (company incorporation — a different regulatory
  phase this blueprint assumes is already complete).
- **Not legal or tax advice.** Every regulatory claim must cite the
  official source and route final filings to Slovak-licensed counsel
  or a registered agent where the law requires licensed representation.

## Capability layer

Resolves via [`kotoba-lang/iso3166`](https://github.com/kotoba-lang/iso3166)
(ISO 3166 `SVK`). Required capabilities:

- :identity
- :forms
- :dmn
- :bpmn
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Slovakia:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
