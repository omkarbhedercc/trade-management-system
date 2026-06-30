# Trade Management System (TMS) — Build Plan

> **Purpose of this repo**: A deliberately *legacy-styled* 3-tier banking application used as a
> **demo target** for the ModernizeX platform — assessment scan, dependency version upgrade, and
> CVE scanning. The app intentionally ships with **outdated, vulnerable dependencies** so the
> scanners have real, name-brand findings (Log4Shell, old Jackson, old Spring, etc.).
>
> This is NOT a production system. It is meant to *look* like a real capital-markets back/front
> office system that a bank (audience: Goldman Sachs) would instantly recognize.

---

## 1. Business Domain

A **Trade Management System** for a capital-markets desk. It captures trades (buy/sell),
maintains reference data (instruments, accounts), and derives **positions / balances** per account.

Core workflows:
1. **Reference data** — maintain instruments (equities, bonds, FX) and client accounts.
2. **Trade booking** — book a buy/sell trade against an instrument + account (the "blotter").
3. **Positions & balances** — derive net quantity, average price, and market value per account.
4. **Dashboard** — summary tiles (total trades, notional, open positions, recent activity).

Keep business logic shallow — the *star of the demo is the scan output*, not the features.

---

## 2. Architecture (3-tier)

```
┌─────────────────────┐      ┌──────────────────────┐      ┌─────────────────────┐
│   Frontend (React)  │ ───▶ │  Backend (Spring     │ ───▶ │  Database           │
│   plain React + CRA │ HTTP │  Boot, Java, REST)   │ JDBC │  PostgreSQL         │
│   :4000             │      │  :4001               │      │  :4002 → 5432       │
└─────────────────────┘      └──────────────────────┘      └─────────────────────┘
```

| Tier      | Tech                         | Port | Notes |
|-----------|------------------------------|------|-------|
| Frontend  | React 16 + react-scripts (CRA), axios, react-router | **4000** | Browser-safe port |
| Backend   | Spring Boot 2.3.x (Java 8), Spring MVC, Spring Data JPA | **4001** | Deliberately old |
| Database  | PostgreSQL 15 (Docker/Podman container) | **4002** | Host 4002 → container 5432 |

### Port notes
Ports **4000 / 4001 / 4002** are used (frontend / backend / database). These are *not* in the
browser unsafe-port list, so the frontend opens normally in Chrome/Firefox. (Avoid 6000 — blocked
as `ERR_UNSAFE_PORT`; on macOS avoid 5000/7000 — AirPlay Receiver — and 9000 — podman gvproxy.)
Change the frontend port via `frontend/.env` (`PORT=4000`) if you need a different value.

---

## 3. Tech Stack & Dependency Versions (INTENTIONALLY OUTDATED)

These versions are chosen so the ModernizeX **CVE scan** and **version-upgrade** flow produce
recognizable, high-severity findings. Do not "fix" them.

### Backend (Maven — `backend/pom.xml`)
| Dependency | Version | Why (demo value) |
|---|---|---|
| Java | **8** | EOL-era runtime; upgrade target → Java 17/21 |
| Spring Boot | **2.3.4.RELEASE** | EOL; upgrade target → Spring Boot 3.x |
| `log4j-core` / `log4j-api` | **2.14.1** | **Log4Shell — CVE-2021-44228** (flagship finding) |
| `jackson-databind` | **2.9.10.4** | Multiple deserialization CVEs |
| `postgresql` (JDBC) | **42.2.5** | Old driver, known CVEs |
| `snakeyaml` | **1.27** | CVE-2022-1471 (RCE on load) |
| `commons-collections` | **3.2.1** | Deserialization gadget CVE |
| Build tool | Maven 3.x | — |

> Note: Spring Boot uses Logback by default. To make **Log4Shell** a real finding, exclude
> `spring-boot-starter-logging` and add `spring-boot-starter-log4j2` pinned to log4j **2.14.1**
> (see §7 pom notes).

### Frontend (`frontend/package.json`)
| Dependency | Version | Why (demo value) |
|---|---|---|
| `react` / `react-dom` | **16.13.1** | Old major; upgrade target → React 18 |
| `react-scripts` | **3.4.1** | Pulls in a large tree of vulnerable transitive deps |
| `axios` | **0.19.2** | SSRF / ReDoS CVEs |
| `lodash` | **4.17.11** | Prototype-pollution CVE-2019-10744 |
| `react-router-dom` | **5.2.0** | Old routing API |

### Database
- PostgreSQL **15** in a container. (DB engine itself can be current; the demo value is in app deps.)

---

## 4. Database Schema (PostgreSQL)

Database: `tms_dev` · user `tms` · password `tms`.

```sql
-- Reference data: tradeable instruments
CREATE TABLE instruments (
    id           BIGSERIAL PRIMARY KEY,
    ticker       VARCHAR(20)  NOT NULL UNIQUE,
    name         VARCHAR(200) NOT NULL,
    asset_class  VARCHAR(20)  NOT NULL,        -- EQUITY | BOND | FX
    currency     VARCHAR(3)   NOT NULL,        -- USD, EUR, GBP...
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

-- Client trading accounts
CREATE TABLE accounts (
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(30)  NOT NULL UNIQUE,
    client_name    VARCHAR(200) NOT NULL,
    account_type   VARCHAR(20)  NOT NULL,      -- CASH | MARGIN
    base_currency  VARCHAR(3)   NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT now()
);

-- Booked trades (the blotter)
CREATE TABLE trades (
    id            BIGSERIAL PRIMARY KEY,
    trade_ref     VARCHAR(30)   NOT NULL UNIQUE,  -- e.g. TRD-000123
    instrument_id BIGINT        NOT NULL REFERENCES instruments(id),
    account_id    BIGINT        NOT NULL REFERENCES accounts(id),
    side          VARCHAR(4)    NOT NULL,         -- BUY | SELL
    quantity      NUMERIC(18,4) NOT NULL,
    price         NUMERIC(18,4) NOT NULL,
    notional      NUMERIC(20,4) NOT NULL,         -- quantity * price
    status        VARCHAR(12)   NOT NULL,         -- NEW | SETTLED | CANCELLED
    trade_date    DATE          NOT NULL,
    booked_at     TIMESTAMP     NOT NULL DEFAULT now()
);

-- Derived positions / balances per account+instrument
CREATE TABLE positions (
    id            BIGSERIAL PRIMARY KEY,
    account_id    BIGINT        NOT NULL REFERENCES accounts(id),
    instrument_id BIGINT        NOT NULL REFERENCES instruments(id),
    net_quantity  NUMERIC(18,4) NOT NULL DEFAULT 0,
    avg_price     NUMERIC(18,4) NOT NULL DEFAULT 0,
    market_value  NUMERIC(20,4) NOT NULL DEFAULT 0,
    updated_at    TIMESTAMP     NOT NULL DEFAULT now(),
    UNIQUE (account_id, instrument_id)
);
```

Positions are recomputed whenever a trade is booked or cancelled:
- BUY increases `net_quantity`, recompute `avg_price` (weighted), recompute `market_value = net_quantity * last_price`.
- SELL decreases `net_quantity`.

Seed data: ~5 instruments (AAPL, MSFT, US10Y, EURUSD, GS), ~3 accounts, a handful of trades.

---

## 5. Backend — Spring Boot

### Package layout (`com.gs.tms`)
```
backend/
├── pom.xml
└── src/main/
    ├── java/com/gs/tms/
    │   ├── TmsApplication.java
    │   ├── config/
    │   │   └── CorsConfig.java            # allow http://localhost:4000
    │   ├── controller/
    │   │   ├── InstrumentController.java
    │   │   ├── AccountController.java
    │   │   ├── TradeController.java
    │   │   ├── PositionController.java
    │   │   └── DashboardController.java
    │   ├── service/
    │   │   ├── InstrumentService.java
    │   │   ├── AccountService.java
    │   │   ├── TradeService.java          # books trade + recomputes positions
    │   │   └── PositionService.java
    │   ├── repository/                    # Spring Data JPA repos
    │   │   ├── InstrumentRepository.java
    │   │   ├── AccountRepository.java
    │   │   ├── TradeRepository.java
    │   │   └── PositionRepository.java
    │   ├── entity/
    │   │   ├── Instrument.java
    │   │   ├── Account.java
    │   │   ├── Trade.java
    │   │   └── Position.java
    │   ├── dto/
    │   │   ├── BookTradeRequest.java
    │   │   ├── TradeResponse.java
    │   │   └── DashboardSummary.java
    │   └── exception/
    │       └── GlobalExceptionHandler.java
    └── resources/
        ├── application.yml
        ├── schema.sql                     # DDL from §4 (or use JPA ddl-auto)
        └── data.sql                       # seed data
```

### `application.yml` (key bits)
```yaml
server:
  port: 4001
spring:
  datasource:
    url: jdbc:postgresql://localhost:4002/tms_dev
    username: tms
    password: tms
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update         # demo only; schema.sql also provided
    show-sql: true
  sql:
    init:
      mode: always             # run schema.sql + data.sql
```

### REST API
Base path `/api`.

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Liveness check |
| GET | `/api/instruments` | List instruments |
| GET | `/api/instruments/{id}` | Get one |
| POST | `/api/instruments` | Create |
| PUT | `/api/instruments/{id}` | Update |
| DELETE | `/api/instruments/{id}` | Delete |
| GET | `/api/accounts` | List accounts |
| GET | `/api/accounts/{id}` | Get one |
| POST | `/api/accounts` | Create |
| PUT | `/api/accounts/{id}` | Update |
| DELETE | `/api/accounts/{id}` | Delete |
| GET | `/api/trades` | List trades (filters: `?accountId=&instrumentId=&status=`) |
| GET | `/api/trades/{id}` | Get one |
| POST | `/api/trades` | **Book a trade** (recomputes positions) |
| POST | `/api/trades/{id}/cancel` | Cancel a trade (recomputes positions) |
| GET | `/api/positions` | List positions (filter: `?accountId=`) |
| GET | `/api/accounts/{id}/positions` | Positions for an account |
| GET | `/api/dashboard/summary` | Counts, total notional, recent trades |

`POST /api/trades` body (`BookTradeRequest`):
```json
{
  "instrumentId": 1,
  "accountId": 1,
  "side": "BUY",
  "quantity": 100,
  "price": 192.50,
  "tradeDate": "2026-06-30"
}
```
Server computes `notional`, generates `trade_ref` (e.g. `TRD-000123`), sets `status=NEW`, and
updates the matching `positions` row.

---

## 6. Frontend — plain React (CRA)

### Layout (`frontend/`)
```
frontend/
├── package.json
├── .env                       # PORT=4000
├── public/index.html
└── src/
    ├── index.js
    ├── App.js                 # react-router routes + nav layout
    ├── api/
    │   └── client.js          # axios instance, baseURL http://localhost:4001/api
    ├── pages/
    │   ├── Dashboard.js       # summary tiles + recent trades
    │   ├── Trades.js          # trade blotter (table + filters)
    │   ├── BookTrade.js       # form to book a new trade
    │   ├── Instruments.js     # reference data CRUD
    │   ├── Accounts.js        # accounts CRUD
    │   └── Positions.js       # positions/balances per account
    └── components/
        ├── NavBar.js
        ├── DataTable.js
        └── StatCard.js
```

### `frontend/.env`
```
PORT=4000
REACT_APP_API_BASE=http://localhost:4001/api
```

### `src/api/client.js`
```js
import axios from "axios";
const client = axios.create({
  baseURL: process.env.REACT_APP_API_BASE || "http://localhost:4001/api",
});
export default client;
```

Pages call the API via `client`. Keep UI plain (tables + forms) so it reads as a dated internal app.

---

## 7. pom.xml notes (making Log4Shell real)

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.3.4.RELEASE</version>
</parent>

<properties>
  <java.version>1.8</java.version>
</properties>

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
      <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
  </dependency>
  <!-- pin vulnerable log4j 2.14.1 (Log4Shell) -->
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.14.1</version>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.2.5</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.10.4</version>
  </dependency>
  <dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>1.27</version>
  </dependency>
  <dependency>
    <groupId>commons-collections</groupId>
    <artifactId>commons-collections</artifactId>
    <version>3.2.1</version>
  </dependency>
</dependencies>
```

---

## 8. Local Development

### Prerequisites
- JDK 8, Maven 3.x
- Node 14–16 (CRA 3.x is happiest on older Node; Node 16 works with `--openssl-legacy-provider` if needed)
- Podman (or Docker)

### Ports
| Service  | Port |
|----------|------|
| Frontend | 4000 |
| Backend  | 4001 |
| Database | 4002 → 5432 (container) |

### Database container
```bash
podman run -d --name tms-postgres \
  -e POSTGRES_DB=tms_dev \
  -e POSTGRES_USER=tms \
  -e POSTGRES_PASSWORD=tms \
  -p 4002:5432 \
  postgres:15-alpine
```

### Backend
```bash
cd backend
mvn spring-boot:run        # serves on :4001
```

### Frontend
```bash
cd frontend
npm install
npm start                  # serves on :4000 (PORT from .env)
```

### Credentials (local)
- PostgreSQL: `tms` / `tms` (db `tms_dev`, port 4002)
- No auth on the app itself (demo simplicity).

---

## 9. Slash Commands

Specialized dev-lifecycle commands live in `.claude/commands/` (ported from the ModernizeX
project and adapted for TMS — single Postgres container `tms-postgres`, tmux session `tms`,
2 panes: frontend + backend):

- `/start-dev` — start `tms-postgres` container + tmux session with frontend & backend
- `/stop-dev` — kill tmux session + stop container (preserves data)
- `/restart-dev` — restart frontend & backend without touching the container or tmux session

---

## 10. Build Order (suggested)

1. Scaffold `backend/` — pom.xml, entities, repos, services, controllers, `application.yml`, `schema.sql`, `data.sql`.
2. Bring up `tms-postgres`, run backend, verify `/api/health` + seeded `/api/instruments`.
3. Scaffold `frontend/` — CRA app, axios client, pages, routing.
4. Wire frontend pages to the API; verify trade booking updates positions.
5. Confirm intentionally-old dependency versions are intact (do NOT upgrade) — this is the demo payload.
6. Push to GitHub; point ModernizeX at the repo and run assessment / CVE / version-upgrade scans.
