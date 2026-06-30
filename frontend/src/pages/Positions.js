import React, { useEffect, useState, useCallback } from "react";
import keyBy from "lodash/keyBy";
import client from "../api/client";
import DataTable from "../components/DataTable";

function fmtMoney(n) {
  if (n == null) return "-";
  return "$" + Number(n).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function Positions() {
  const [positions, setPositions] = useState([]);
  const [accounts, setAccounts] = useState([]);
  const [instruments, setInstruments] = useState([]);
  const [accountId, setAccountId] = useState("");
  const [error, setError] = useState(null);

  const load = useCallback(() => {
    const params = {};
    if (accountId) params.accountId = accountId;
    client
      .get("/positions", { params })
      .then((res) => setPositions(res.data))
      .catch((e) => setError(e.message));
  }, [accountId]);

  useEffect(() => {
    client.get("/accounts").then((r) => setAccounts(r.data));
    client.get("/instruments").then((r) => setInstruments(r.data));
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const acctById = keyBy(accounts, "id");
  const instrById = keyBy(instruments, "id");

  const columns = [
    { key: "account", label: "Account", render: (r) => (acctById[r.accountId] ? acctById[r.accountId].accountNumber : r.accountId) },
    { key: "instrument", label: "Instrument", render: (r) => (instrById[r.instrumentId] ? instrById[r.instrumentId].ticker : r.instrumentId) },
    { key: "netQuantity", label: "Net Qty" },
    { key: "avgPrice", label: "Avg Price", render: (r) => fmtMoney(r.avgPrice) },
    { key: "marketValue", label: "Market Value", render: (r) => fmtMoney(r.marketValue) },
  ];

  return (
    <div>
      <h2>Positions &amp; Balances</h2>
      {error && <div className="msg error">{error}</div>}
      <div className="toolbar">
        <div className="field">
          <label>Account</label>
          <select value={accountId} onChange={(e) => setAccountId(e.target.value)}>
            <option value="">All</option>
            {accounts.map((a) => (
              <option key={a.id} value={a.id}>{a.accountNumber}</option>
            ))}
          </select>
        </div>
      </div>
      <DataTable columns={columns} rows={positions} emptyText="No positions." />
    </div>
  );
}
