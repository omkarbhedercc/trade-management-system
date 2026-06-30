import React, { useEffect, useState, useCallback } from "react";
import client from "../api/client";
import DataTable from "../components/DataTable";

function fmtMoney(n) {
  if (n == null) return "-";
  return "$" + Number(n).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function Trades() {
  const [trades, setTrades] = useState([]);
  const [accounts, setAccounts] = useState([]);
  const [instruments, setInstruments] = useState([]);
  const [filters, setFilters] = useState({ accountId: "", instrumentId: "", status: "" });
  const [error, setError] = useState(null);

  const load = useCallback(() => {
    const params = {};
    if (filters.accountId) params.accountId = filters.accountId;
    if (filters.instrumentId) params.instrumentId = filters.instrumentId;
    if (filters.status) params.status = filters.status;
    client
      .get("/trades", { params })
      .then((res) => setTrades(res.data))
      .catch((e) => setError(e.message));
  }, [filters]);

  useEffect(() => {
    client.get("/accounts").then((r) => setAccounts(r.data));
    client.get("/instruments").then((r) => setInstruments(r.data));
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const cancel = (id) => {
    client
      .post(`/trades/${id}/cancel`)
      .then(() => load())
      .catch((e) => setError(e.message));
  };

  const columns = [
    { key: "tradeRef", label: "Ref" },
    { key: "tradeDate", label: "Date" },
    { key: "side", label: "Side", render: (r) => <span className={"badge " + r.side}>{r.side}</span> },
    { key: "instrumentTicker", label: "Instrument" },
    { key: "accountNumber", label: "Account" },
    { key: "quantity", label: "Qty" },
    { key: "price", label: "Price", render: (r) => fmtMoney(r.price) },
    { key: "notional", label: "Notional", render: (r) => fmtMoney(r.notional) },
    { key: "status", label: "Status", render: (r) => <span className={"badge " + r.status}>{r.status}</span> },
    {
      key: "actions",
      label: "Actions",
      render: (r) =>
        r.status === "CANCELLED" ? (
          <span className="muted">—</span>
        ) : (
          <button className="link" onClick={() => cancel(r.id)}>cancel</button>
        ),
    },
  ];

  return (
    <div>
      <h2>Trade Blotter</h2>
      {error && <div className="msg error">{error}</div>}
      <div className="toolbar">
        <div className="field">
          <label>Account</label>
          <select value={filters.accountId} onChange={(e) => setFilters({ ...filters, accountId: e.target.value })}>
            <option value="">All</option>
            {accounts.map((a) => (
              <option key={a.id} value={a.id}>{a.accountNumber}</option>
            ))}
          </select>
        </div>
        <div className="field">
          <label>Instrument</label>
          <select value={filters.instrumentId} onChange={(e) => setFilters({ ...filters, instrumentId: e.target.value })}>
            <option value="">All</option>
            {instruments.map((i) => (
              <option key={i.id} value={i.id}>{i.ticker}</option>
            ))}
          </select>
        </div>
        <div className="field">
          <label>Status</label>
          <select value={filters.status} onChange={(e) => setFilters({ ...filters, status: e.target.value })}>
            <option value="">All</option>
            <option value="NEW">NEW</option>
            <option value="SETTLED">SETTLED</option>
            <option value="CANCELLED">CANCELLED</option>
          </select>
        </div>
        <button className="secondary" onClick={() => setFilters({ accountId: "", instrumentId: "", status: "" })}>
          Clear
        </button>
      </div>
      <DataTable columns={columns} rows={trades} emptyText="No trades match the filters." />
    </div>
  );
}
