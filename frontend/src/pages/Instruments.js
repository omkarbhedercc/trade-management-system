import React, { useEffect, useState } from "react";
import client from "../api/client";
import DataTable from "../components/DataTable";

const EMPTY = { ticker: "", name: "", assetClass: "EQUITY", currency: "USD" };

export default function Instruments() {
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(EMPTY);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState(null);

  const load = () => {
    client.get("/instruments").then((r) => setRows(r.data)).catch((e) => setError(e.message));
  };

  useEffect(() => { load(); }, []);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const reset = () => { setForm(EMPTY); setEditingId(null); };

  const submit = (e) => {
    e.preventDefault();
    setError(null);
    const req = editingId
      ? client.put(`/instruments/${editingId}`, form)
      : client.post("/instruments", form);
    req.then(() => { reset(); load(); }).catch((err) => {
      const msg = err.response && err.response.data ? (err.response.data.message || err.message) : err.message;
      setError(msg);
    });
  };

  const edit = (row) => {
    setEditingId(row.id);
    setForm({ ticker: row.ticker, name: row.name, assetClass: row.assetClass, currency: row.currency });
  };

  const remove = (id) => {
    setError(null);
    client.delete(`/instruments/${id}`).then(load).catch((err) => {
      const msg = err.response && err.response.data ? (err.response.data.message || err.message) : err.message;
      setError(msg);
    });
  };

  const columns = [
    { key: "id", label: "ID" },
    { key: "ticker", label: "Ticker" },
    { key: "name", label: "Name" },
    { key: "assetClass", label: "Asset Class" },
    { key: "currency", label: "Ccy" },
    {
      key: "actions",
      label: "Actions",
      render: (r) => (
        <span>
          <button className="link" onClick={() => edit(r)}>edit</button>
          <button className="link" onClick={() => remove(r.id)}>delete</button>
        </span>
      ),
    },
  ];

  return (
    <div>
      <h2>Instruments</h2>
      {error && <div className="msg error">{error}</div>}
      <form className="panel" onSubmit={submit} style={{ marginBottom: 20 }}>
        <h3>{editingId ? "Edit instrument #" + editingId : "Add instrument"}</h3>
        <div className="field">
          <label>Ticker</label>
          <input value={form.ticker} onChange={set("ticker")} required />
        </div>
        <div className="field">
          <label>Name</label>
          <input value={form.name} onChange={set("name")} required />
        </div>
        <div className="field">
          <label>Asset Class</label>
          <select value={form.assetClass} onChange={set("assetClass")}>
            <option value="EQUITY">EQUITY</option>
            <option value="BOND">BOND</option>
            <option value="FX">FX</option>
          </select>
        </div>
        <div className="field">
          <label>Currency</label>
          <input value={form.currency} onChange={set("currency")} maxLength={3} required />
        </div>
        <button type="submit">{editingId ? "Save" : "Add"}</button>
        {editingId && <button type="button" className="secondary" style={{ marginLeft: 8 }} onClick={reset}>Cancel</button>}
      </form>
      <DataTable columns={columns} rows={rows} emptyText="No instruments." />
    </div>
  );
}
