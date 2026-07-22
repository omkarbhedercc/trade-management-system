# Trade Management System (TMS)

A deliberately **legacy-styled 3-tier capital-markets application** — a demo target for the
**ModernizeX** platform (assessment scan, dependency-version upgrade, CVE scanning). It ships with
intentionally outdated, vulnerable dependencies (Log4Shell, old Jackson, old Spring, etc.) so the
scanners produce real, name-brand findings.

> Not a production system. See [plan.md](plan.md) for the full build spec.

---

## Architecture & Flow

```
                          👤  User / Browser
                                 │  clicks
                                 ▼
┌───────────────────────────┐   HTTP / JSON    ┌────────────────────────────────┐    JDBC     ┌─────────────────────────┐
│  FRONTEND                 │  ─────────────▶  │  BACKEND                       │  ────────▶  │  DATABASE               │
│  React 16 · CRA           │                  │  Spring Boot 2.3 · Java 8      │             │  PostgreSQL 15          │
│  :4000                    │  ◀─────────────  │  :4001                         │  ◀────────  │  :4002 → 5432           │
└───────────────────────────┘    responses     └────────────────────────────────┘    rows     └─────────────────────────┘
             │                                                │                                            │
      Pages                                          REST Controllers  /api/**                       Tables
        • Dashboard                                          │                                        • instruments
        • Trades (blotter)                                   ▼                                        • accounts
        • Book Trade                                  Services                                        • trades
        • Positions                                    └─ TradeService → recomputes positions         • positions
        • Instruments                                        │
        • Accounts                                           ▼
             │                                        Spring Data JPA Repositories
             ▼
      axios client (baseURL = /api)                  Swagger UI  →  /swagger-ui.html
```

### Trade-booking flow (the core loop)

1. **Book Trade** page submits `POST /api/trades` via the axios client.
2. `TradeController` → `TradeService` validates, computes `notional`, generates a `trade_ref`
   (e.g. `TRD-000008`), persists the trade with status `NEW`.
3. `TradeService` **recomputes the position** for that account + instrument
   (net quantity, weighted average price, market value) and saves it via `PositionRepository`.
4. **Positions** and **Dashboard** pages re-fetch and reflect the change.
   Cancelling a trade (`POST /api/trades/{id}/cancel`) reverses the effect the same way.

---

## Tech stack (intentionally outdated — the scan payload)

| Tier | Tech | Port | Notable CVE / issue |
|------|------|------|---------------------|
| Frontend | React 16.13.1, react-scripts 3.4.1, axios 0.19.2, lodash 4.17.11 | **4000** | axios SSRF/ReDoS; lodash prototype pollution (CVE-2019-10744) |
| Backend | Spring Boot 2.3.4 (Java 8), log4j 2.14.1, jackson-databind 2.9.10.4, snakeyaml 1.27, commons-collections 3.2.1, postgresql 42.2.5 | **4001** | **Log4Shell — CVE-2021-44228**; Jackson/snakeyaml/commons-collections deserialization RCE |
| Database | PostgreSQL 15 (container) | **4002** → 5432 | Engine current; value is in the app deps |

> A live version of this table is available in-app via the floating **stack & CVE** button
> (bottom-right on every page).

---

## Run it

Ports are browser-safe (not `ERR_UNSAFE_PORT`). Use the dev skills:

- `/start-dev` — starts the `tms-postgres` container + frontend & backend in a `tms` tmux session
- `/restart-dev` — restart services without touching the container
- `/stop-dev` — stop everything (preserves data)

| Service | URL |
|---------|-----|
| Frontend | http://localhost:4000 |
| Backend health | http://localhost:4001/api/health |
| API docs (Swagger) | http://localhost:4001/swagger-ui.html |
| Database | `jdbc:postgresql://localhost:4002/tms_dev` (`tms` / `tms`) |

test rerun test two rerun
