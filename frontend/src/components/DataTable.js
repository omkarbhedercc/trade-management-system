import React from "react";

/**
 * Minimal table. columns: [{ key, label, render? }]
 */
export default function DataTable({ columns, rows, emptyText }) {
  if (!rows || rows.length === 0) {
    return <p className="muted">{emptyText || "No records."}</p>;
  }
  return (
    <table>
      <thead>
        <tr>
          {columns.map((c) => (
            <th key={c.key}>{c.label}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {rows.map((row, i) => (
          <tr key={row.id != null ? row.id : i}>
            {columns.map((c) => (
              <td key={c.key}>{c.render ? c.render(row) : row[c.key]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
