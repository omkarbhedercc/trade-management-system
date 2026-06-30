import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import client from "../api/client";

function today() {
  return new Date().toISOString().slice(0, 10);
}

export default function BookTrade() {
  const history = useHistory();
  const [instruments, setInstruments] = useState([]);
  const [accounts, setAccounts] = useState([]);
  const [form, setForm] = useState({
    instrumentId: "",
    accountId: "",
    side: "BUY",
    quantity: "",
    price: "",
    tradeDate: today(),
  });
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    client.get("/instruments").then((r) => setInstruments(r.data));
    client.get("/accounts").then((r) => setAccounts(r.data));
  }, []);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const notional = form.quantity && form.price ? (Number(form.quantity) * Number(form.price)) : 0;

  const submit = (e) => {
    e.preventDefault();
    setError(null);
    setResult(null);
    setSubmitting(true);
    client
      .post("/trades", {
        instrumentId: Number(form.instrumentId),
        accountId: Number(form.accountId),
        side: form.side,
        quantity: Number(form.quantity),
        price: Number(form.price),
        tradeDate: form.tradeDate,
      })
      .then((res) => {
        setResult(res.data);
        setSubmitting(false);
      })
      .catch((e) => {
        const msg = e.response && e.response.data ? (e.response.data.message || JSON.stringify(e.response.data)) : e.message;
        setError(msg);
        setSubmitting(false);
      });
  };

  return (
    <div>
      <h2>Book Trade</h2>
      {error && <div className="msg error">{error}</div>}
      {result && (
        <div className="msg success">
          Booked <strong>{result.tradeRef}</strong> — {result.side} {result.quantity} {result.instrumentTicker} @ {result.price}.{" "}
          <button className="link" onClick={() => history.push("/positions")}>view positions</button>
          {" · "}
          <button className="link" onClick={() => history.push("/trades")}>view blotter</button>
        </div>
      )}
      <form className="panel" onSubmit={submit}>
        <div className="field">
          <label>Instrument</label>
          <select value={form.instrumentId} onChange={set("instrumentId")} required>
            <option value="">— select —</option>
            {instruments.map((i) => (
              <option key={i.id} value={i.id}>{i.ticker} — {i.name}</option>
            ))}
          </select>
        </div>
        <div className="field">
          <label>Account</label>
          <select value={form.accountId} onChange={set("accountId")} required>
            <option value="">— select —</option>
            {accounts.map((a) => (
              <option key={a.id} value={a.id}>{a.accountNumber} — {a.clientName}</option>
            ))}
          </select>
        </div>
        <div className="field">
          <label>Side</label>
          <select value={form.side} onChange={set("side")}>
            <option value="BUY">BUY</option>
            <option value="SELL">SELL</option>
          </select>
        </div>
        <div className="field">
          <label>Quantity</label>
          <input type="number" step="any" min="0" value={form.quantity} onChange={set("quantity")} required />
        </div>
        <div className="field">
          <label>Price</label>
          <input type="number" step="any" min="0" value={form.price} onChange={set("price")} required />
        </div>
        <div className="field">
          <label>Trade Date</label>
          <input type="date" value={form.tradeDate} onChange={set("tradeDate")} required />
        </div>
        <div className="field">
          <label>Notional (computed)</label>
          <input type="text" value={notional ? notional.toFixed(2) : ""} readOnly disabled />
        </div>
        <button type="submit" disabled={submitting}>{submitting ? "Booking…" : "Book Trade"}</button>
      </form>
    </div>
  );
}
