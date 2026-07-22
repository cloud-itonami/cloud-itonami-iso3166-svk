# cloud-itonami-iso3166-svk

Open ISO 3166 Blueprint for **SVK**: Slovak Republic (EU member state).

**`:implemented`** for **SVK**. Two flagship governor checks:
`fdi-screening-missing` (Zákon č. 497/2022 Z. z. o preverovaní
zahraničných investícií) and `platform-operator-fused` (ÚVO vs. Úrad
vlády SR -- see below).

```
clojure -M:dev:test    # governor contract + facts + phase + registry + store + statute + culture
clojure -M:dev:run     # walk a demo engagement through the full actor graph
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

## Implementation status

**`:implemented`.** `src/marketentry/*` is a running langgraph-clj
StateGraph actor (`operation/build`): a MarketEntry-LLM advisor
(`marketentryllm.cljc`) sealed into a single `:advise` node, whose
proposal is ALWAYS routed through the Market-Entry Compliance
Governor (`governor.cljc`) and the rollout phase gate (`phase.cljc`)
before anything touches the SSoT (`store.cljc`, MemStore +
DatomicStore via `io.github.kotoba-lang/langchain-store`).

### Governor checks (priority order, all HARD -- unoverridable by a human approver)

| # | Check | Grounded in |
|---|-------|-------------|
| 1 | Spec-basis (no fabricated jurisdiction) | `marketentry.facts/spec-basis` |
| 2 | Evidence incomplete | the jurisdiction's `:required-evidence` checklist |
| 3 | `:fdi-screening-missing` (**flagship #1**) | Zákon č. 497/2022 Z. z., Ministerstvo hospodárstva SR |
| 4 | `:platform-operator-fused` (**flagship #2**) | ÚVO/IS EVO-EPVO regulator-operator split -- see below |
| 5 | Engagement-fee mismatch | independent recompute (`base-fee + monthly-rate x monitoring-months`) |
| 6 | `:ico-unverified` | Štatistický úrad SR (Statistical Office) -- IČO |
| 7 | `:dic-unverified` | Finančná správa -- DIČ |
| 8 | Confidence floor / actuation gate | `:filing/draft`/`:filing/submit` always escalate |
| — | Double-draft / double-submit guards | dedicated `:drafted?`/`:submitted?` facts |

### Flagship check #1: FDI screening

An engagement flagged `:requires-fdi-screening? true` (a "kritická
zahraničná investícia" per nariadenie vlády SR č. 61/2023 Z. z.) must
have independently confirmed `:fdi-screening-cleared? true` before
`:filing/submit` -- grounded in Zákon č. 497/2022 Z. z. o preverovaní
zahraničných investícií (effective 1. 3. 2023, implementing EU
Regulation 2019/452), administered by Ministerstvo hospodárstva SR.
See `test/marketentry/governor_contract_test.clj`'s
`fdi-screening-missing-is-held-and-unoverridable`.

### Flagship check #2: ÚVO is not Úrad vlády SR

The central fabrication trap for this jurisdiction is collapsing two
different authorities into one -- the same class of mistake
`cloud-itonami-iso3166-lva`'s IUB/VDAA check exists to catch,
independently reconfirmed for Slovakia. This actor keeps them apart on
purpose, in the catalog (`marketentry.facts/platform-operator-spec-basis`)
and in a dedicated governor check
(`marketentry.governor/platform-operator-fusion-violations`):

- **ÚVO** (Úrad pre verejné obstarávanie -- Public Procurement Office)
  is the **legal/regulatory-oversight authority**: it administers
  Zákon č. 343/2015 Z. z. o verejnom obstarávaní and its own
  eForms/IS ÚVO notice-publication system, and runs the "Zoznam
  hospodárskych subjektov" (List of Economic Operators, § 187 ods. 7).
- **IS EVO/EPVO** (Elektronická platforma verejného obstarávania,
  [isepvo.sk](https://www.isepvo.sk/)), the transactional
  e-tendering platform, is **NOT operated by ÚVO**. As of **31 March
  2022**, its management transferred FROM ÚVO TO **Úrad vlády
  Slovenskej republiky** (Office of the Government of the Slovak
  Republic) -- ÚVO's own site explicitly states it no longer operates
  this system, retaining only the supervisory/appeals (dohľad,
  námietky) authority. A third system, EKS (eks.sk), is a separate
  below-threshold e-marketplace -- this catalog does not assert a
  specific current operator for EKS (not independently confirmed).

A `:jurisdiction/assess` proposal that states or implies "ÚVO
operates IS EVO/EPVO" -- omits the distinction, fuses the two
authorities into one value, or cites either against the wrong
catalogued value -- is a HARD violation the governor rejects
unconditionally
(`fused-platform-authority-claim-is-held-and-unoverridable` and
`clean-assess-correctly-distinguishes-uvo-from-urad-vlady-sr`).

### Sources cited per check

- Business registration: Obchodný register (Business Register),
  [orsr.sk](https://www.orsr.sk/), administered by the Ministerstvo
  spravodlivosti SR (Ministry of Justice) via regional registry
  courts, under Zákon č. 530/2003 Z. z.
- Procurement law: Zákon č. 343/2015 Z. z. o verejnom obstarávaní --
  [uvo.gov.sk](https://www.uvo.gov.sk/); oversight/notices -- ÚVO
- E-procurement platform: IS EVO/EPVO --
  [isepvo.sk](https://www.isepvo.sk/), operated by Úrad vlády
  Slovenskej republiky (since 31 March 2022), NOT ÚVO
- FDI screening: Zákon č. 497/2022 Z. z. --
  [economy.gov.sk](https://www.economy.gov.sk/podnikatelske-prostredie/preverovanie-zahranicnych-investicii/zakladne-informacie),
  Ministerstvo hospodárstva SR
- IČO: Štatistický úrad Slovenskej republiky (Statistical Office) --
  [statistics.sk](https://www.statistics.sk/) -- a body DISTINCT from
  Finančná správa
- DIČ / IČ DPH: Finančná správa --
  [financnasprava.sk](https://www.financnasprava.sk/), Zákon č.
  563/2009 Z. z. (DIČ) and Zákon č. 222/2004 Z. z. (IČ DPH, mandatory
  once turnover exceeds 50 000 EUR/62 500 EUR)

### Actuation

- `:engagement/intake` may auto-commit at phase 3 when the governor is
  clean (no portal-facing risk).
- `:jurisdiction/assess` always escalates to human approval, at every
  phase, even when clean.
- `:filing/draft` and `:filing/submit` are **permanently excluded**
  from every phase's `:auto` set (`phase.cljc`) AND are members of the
  governor's own `high-stakes` set (`governor.cljc`) that forces
  escalation independently of phase. Two layers, not one, agree that
  drafting a real EPVO portal package or submitting a real EPVO
  portal registration is always a human market-entry operator's call.
- Every HARD violation is unoverridable: a human approver sees the
  `:hold` disposition and its `:violations`, but cannot commit past a
  HARD check. Only the confidence/actuation escalation is a genuine
  human decision point (`:approved`/rejected via `:request-approval`).
- Every commit or hold appends exactly one fact to the append-only
  ledger (`store/append-ledger!`, called from both the `:commit` and
  `:hold` StateGraph nodes) — nothing is ever rewritten or removed.

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
