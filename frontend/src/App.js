import React from "react";
import { Switch, Route } from "react-router-dom";
import NavBar from "./components/NavBar";
import DemoInfoButton from "./components/DemoInfoButton";
import Dashboard from "./pages/Dashboard";
import Trades from "./pages/Trades";
import BookTrade from "./pages/BookTrade";
import Positions from "./pages/Positions";
import Instruments from "./pages/Instruments";
import Accounts from "./pages/Accounts";

export default function App() {
  return (
    <div>
      <NavBar />
      <div className="container">
        <Switch>
          <Route exact path="/" component={Dashboard} />
          <Route path="/trades" component={Trades} />
          <Route path="/book" component={BookTrade} />
          <Route path="/positions" component={Positions} />
          <Route path="/instruments" component={Instruments} />
          <Route path="/accounts" component={Accounts} />
        </Switch>
      </div>
      <DemoInfoButton />
    </div>
  );
}
