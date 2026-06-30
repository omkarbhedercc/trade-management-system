import React, { useEffect, useState } from "react";
import client from "../api/client";
import DataTable from "../components/DataTable";

const EMPTY = { accountNumber: "", clientName: "", accountType: "CASH", baseCurrency: "USD" };

export default function Accounts() {
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(EMPTY);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState(null);

  const load = () => {
    client.get("/accounts").then((r) => setRows(r.data)).catch((e) => setError(e.message));
  };

  useEffect(() => { load(); }, []);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const reset = () => { setForm(EMPTY); setEditingId(null); };

  const submit = (e) => {
    e.preventDefault();
    setError(null);
    const req = editingId
      ? client.put(`/accounts/${editingId}`, form)
      : client.post("/accounts", form);
    req.then(() => { reset(); load(); }).catch((err) => {
      const msg = err.response && err.response.data ? (err.response.data.message || err.message) : err.message;
      setError(msg);
    });
  };

  const edit = (row) => {
    setEditingId(row.id);
    setForm({
      accountNumber: row.accountNumber,
      clientName: row.clientName,
      accountType: row.accountType,
      baseCurrency: row.baseCurrency,
    });
  };

  const remove = (id) => {
    setError(null);
    client.delete(`/accounts/${id}`).then(load).catch((err) => {
      const msg = err.response && err.response.data ? (err.response.data.message || err.message) : err.message;
      setError(msg);
    });
  };

  const columns = [
    { key: "id", label: "ID" },
    { key: "accountNumber", label: "Account #" },
    { key: "clientName", label: "Client" },
    { key: "accountType", label: "Type" },
    { key: "baseCurrency", label: "Ccy" },
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
      <h2>Accounts</h2>
      {error && <div className="msg error">{error}</div>}
      <form className="panel" onSubmit={submit} style={{ marginBottom: 20 }}>
        <h3>{editingId ? "Edit account #" + editingId : "Add account"}</h3>
        <div className="field">
          <label>Account Number</label>
          <input value={form.accountNumber} onChange={set("accountNumber")} required />
        </div>
        <div className="field">
          <label>Client Name</label>
          <input value={form.clientName} onChange={set("clientName")} required />
        </div>
        <div className="field">
          <label>Account Type</label>
          <select value={form.accountType} onChange={set("accountType")}>
            <option value="CASH">CASH</option>
            <option value="MARGIN">MARGIN</option>
          </select>
        </div>
        <div className="field">
          <label>Base Currency</label>
          <input value={form.baseCurrency} onChange={set("baseCurrency")} maxLength={3} required />
        </div>
        <button type="submit">{editingId ? "Save" : "Add"}</button>
        {editingId && <button type="button" className="secondary" style={{ marginLeft: 8 }} onClick={reset}>Cancel</button>}
      </form>
      <DataTable columns={columns} rows={rows} emptyText="No accounts." />
    </div>
  );
}
