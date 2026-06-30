import React from "react";
import { NavLink } from "react-router-dom";

export default function NavBar() {
  return (
    <div className="nav">
      <span className="brand">TMS</span>
      <NavLink exact to="/" activeClassName="active">Dashboard</NavLink>
      <NavLink to="/trades" activeClassName="active">Trades</NavLink>
      <NavLink to="/book" activeClassName="active">Book Trade</NavLink>
      <NavLink to="/positions" activeClassName="active">Positions</NavLink>
      <NavLink to="/instruments" activeClassName="active">Instruments</NavLink>
      <NavLink to="/accounts" activeClassName="active">Accounts</NavLink>
    </div>
  );
}
