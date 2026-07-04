import React from "react";
import PropTypes from "prop-types";

/**
 * Minimal table. columns: [{ key, label, render? }]
 */
function DataTable({ columns, rows, emptyText }) {
  if (!rows || rows.length === 0) {
    return <p className="muted">{emptyText || "No records."}</p>;
  }
  return (
    <table>

DataTable.propTypes = {
  columns: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
      render: PropTypes.func
    })
  ).isRequired,
  rows: PropTypes.array,
  emptyText: PropTypes.string
};

export default DataTable;