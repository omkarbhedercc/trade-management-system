-- Trade Management System schema (PostgreSQL)
-- Idempotent: safe to run on every boot.

CREATE TABLE IF NOT EXISTS instruments (
    id           BIGSERIAL PRIMARY KEY,
    ticker       VARCHAR(20)  NOT NULL UNIQUE,
    name         VARCHAR(200) NOT NULL,
    asset_class  VARCHAR(20)  NOT NULL,        -- EQUITY | BOND | FX
    currency     VARCHAR(3)   NOT NULL,        -- USD, EUR, GBP...
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS accounts (
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(30)  NOT NULL UNIQUE,
    client_name    VARCHAR(200) NOT NULL,
    account_type   VARCHAR(20)  NOT NULL,      -- CASH | MARGIN
    base_currency  VARCHAR(3)   NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS trades (
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

CREATE TABLE IF NOT EXISTS positions (
    id            BIGSERIAL PRIMARY KEY,
    account_id    BIGINT        NOT NULL REFERENCES accounts(id),
    instrument_id BIGINT        NOT NULL REFERENCES instruments(id),
    net_quantity  NUMERIC(18,4) NOT NULL DEFAULT 0,
    avg_price     NUMERIC(18,4) NOT NULL DEFAULT 0,
    market_value  NUMERIC(20,4) NOT NULL DEFAULT 0,
    updated_at    TIMESTAMP     NOT NULL DEFAULT now(),
    UNIQUE (account_id, instrument_id)
);
