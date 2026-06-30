import React, { useEffect, useState } from "react";
import client from "../api/client";
import StatCard from "../components/StatCard";
import DataTable from "../components/DataTable";

function fmtMoney(n) {
  if (n == null) return "-";
  return "$" + Number(n).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function Dashboard() {
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    client
      .get("/dashboard/summary")
      .then((res) => setSummary(res.data))
      .catch((e) => setError(e.message));
  }, []);

  if (error) return <div className="msg error">Failed to load dashboard: {error}</div>;
  if (!summary) return <p className="muted">Loading…</p>;

  const columns = [
    { key: "tradeRef", label: "Ref" },
    { key: "side", label: "Side", render: (r) => <span className={"badge " + r.side}>{r.side}</span> },
    { key: "instrumentTicker", label: "Instrument" },
    { key: "accountNumber", label: "Account" },
    { key: "quantity", label: "Qty" },
    { key: "price", label: "Price", render: (r) => fmtMoney(r.price) },
    { key: "notional", label: "Notional", render: (r) => fmtMoney(r.notional) },
    { key: "status", label: "Status", render: (r) => <span className={"badge " + r.status}>{r.status}</span> },
  ];

  return (
    <div>
      <h2>Dashboard</h2>
      <div className="cards">
        <StatCard label="Total Trades" value={summary.totalTrades} />
        <StatCard label="Total Notional" value={fmtMoney(summary.totalNotional)} />
        <StatCard label="Open Positions" value={summary.openPositions} />
        <StatCard label="Instruments" value={summary.instrumentCount} />
        <StatCard label="Accounts" value={summary.accountCount} />
      </div>
      <h3>Recent Activity</h3>
      <DataTable columns={columns} rows={summary.recentTrades} emptyText="No trades booked yet." />
    </div>
  );
}
