-- Seed reference data, a handful of trades, and their derived positions.
-- Idempotent: explicit ids + ON CONFLICT DO NOTHING, sequences re-synced at the end.

-- Instruments -------------------------------------------------------------
INSERT INTO instruments (id, ticker, name, asset_class, currency) VALUES
    (1, 'AAPL',   'Apple Inc.',                 'EQUITY', 'USD'),
    (2, 'MSFT',   'Microsoft Corp.',            'EQUITY', 'USD'),
    (3, 'US10Y',  'US Treasury 10Y Note',       'BOND',   'USD'),
    (4, 'EURUSD', 'Euro / US Dollar',           'FX',     'USD'),
    (5, 'GS',     'Goldman Sachs Group Inc.',   'EQUITY', 'USD')
ON CONFLICT (ticker) DO NOTHING;

-- Accounts ----------------------------------------------------------------
INSERT INTO accounts (id, account_number, client_name, account_type, base_currency) VALUES
    (1, 'ACC-1001', 'GS Proprietary Desk', 'MARGIN', 'USD'),
    (2, 'ACC-1002', 'BlackRock Global Fund', 'CASH', 'USD'),
    (3, 'ACC-1003', 'Vanguard Index Trust', 'CASH', 'USD')
ON CONFLICT (account_number) DO NOTHING;

-- Trades (the blotter) ----------------------------------------------------
INSERT INTO trades (id, trade_ref, instrument_id, account_id, side, quantity, price, notional, status, trade_date) VALUES
    (1, 'TRD-000001', 1, 1, 'BUY',  100, 190.0000, 19000.0000, 'NEW', DATE '2026-06-25'),
    (2, 'TRD-000002', 1, 1, 'BUY',   50, 200.0000, 10000.0000, 'NEW', DATE '2026-06-26'),
    (3, 'TRD-000003', 2, 1, 'BUY',   80, 410.0000, 32800.0000, 'NEW', DATE '2026-06-26'),
    (4, 'TRD-000004', 5, 2, 'BUY',   40, 470.0000, 18800.0000, 'NEW', DATE '2026-06-27'),
    (5, 'TRD-000005', 5, 2, 'SELL',  10, 480.0000,  4800.0000, 'NEW', DATE '2026-06-29'),
    (6, 'TRD-000006', 3, 3, 'BUY', 1000,  98.5000, 98500.0000, 'NEW', DATE '2026-06-29')
ON CONFLICT (trade_ref) DO NOTHING;

-- Derived positions -------------------------------------------------------
-- acct1/AAPL: net 150, avg (100*190 + 50*200)/150 = 193.3333, mv 150 * last(200) = 30000
-- acct1/MSFT: net 80, avg 410, mv 80 * 410 = 32800
-- acct2/GS:   net 30 (40 buy - 10 sell), avg 470, mv 30 * last(480) = 14400
-- acct3/US10Y:net 1000, avg 98.50, mv 1000 * 98.50 = 98500
INSERT INTO positions (account_id, instrument_id, net_quantity, avg_price, market_value) VALUES
    (1, 1, 150.0000, 193.3333, 30000.0000),
    (1, 2,  80.0000, 410.0000, 32800.0000),
    (2, 5,  30.0000, 470.0000, 14400.0000),
    (3, 3, 1000.0000, 98.5000, 98500.0000)
ON CONFLICT (account_id, instrument_id) DO NOTHING;

-- Re-sync sequences so app-generated inserts don't collide with seeded ids.
SELECT setval('instruments_id_seq', (SELECT COALESCE(MAX(id), 1) FROM instruments));
SELECT setval('accounts_id_seq',    (SELECT COALESCE(MAX(id), 1) FROM accounts));
SELECT setval('trades_id_seq',      (SELECT COALESCE(MAX(id), 1) FROM trades));
SELECT setval('positions_id_seq',   (SELECT COALESCE(MAX(id), 1) FROM positions));
