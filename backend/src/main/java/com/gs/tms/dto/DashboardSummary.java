package com.gs.tms.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummary {

    private long totalTrades;
    private BigDecimal totalNotional;
    private long openPositions;
    private long instrumentCount;
    private long accountCount;
    private List<TradeResponse> recentTrades;

    public long getTotalTrades() { return totalTrades; }
    public void setTotalTrades(long totalTrades) { this.totalTrades = totalTrades; }

    public BigDecimal getTotalNotional() { return totalNotional; }
    public void setTotalNotional(BigDecimal totalNotional) { this.totalNotional = totalNotional; }

    public long getOpenPositions() { return openPositions; }
    public void setOpenPositions(long openPositions) { this.openPositions = openPositions; }

    public long getInstrumentCount() { return instrumentCount; }
    public void setInstrumentCount(long instrumentCount) { this.instrumentCount = instrumentCount; }

    public long getAccountCount() { return accountCount; }
    public void setAccountCount(long accountCount) { this.accountCount = accountCount; }

    public List<TradeResponse> getRecentTrades() { return recentTrades; }
    public void setRecentTrades(List<TradeResponse> recentTrades) { this.recentTrades = recentTrades; }
}
