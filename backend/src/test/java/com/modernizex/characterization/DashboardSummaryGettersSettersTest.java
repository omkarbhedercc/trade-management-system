package com.modernizex.characterization;

import com.gs.tms.dto.DashboardSummary;
import com.gs.tms.dto.TradeResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardSummaryGettersSettersTest {

    @Test
    public void testSetAndGetTotalTrades() {
        DashboardSummary s = new DashboardSummary();
        s.setTotalTrades(100L);
        assertEquals(100L, s.getTotalTrades());
    }

    @Test
    public void testSetAndGetTotalNotional() {
        DashboardSummary s = new DashboardSummary();
        BigDecimal notional = new BigDecimal("1234567.89");
        s.setTotalNotional(notional);
        assertEquals(notional, s.getTotalNotional());
    }

    @Test
    public void testSetAndGetOpenPositions() {
        DashboardSummary s = new DashboardSummary();
        s.setOpenPositions(5L);
        assertEquals(5L, s.getOpenPositions());
    }

    @Test
    public void testSetAndGetInstrumentCount() {
        DashboardSummary s = new DashboardSummary();
        s.setInstrumentCount(20L);
        assertEquals(20L, s.getInstrumentCount());
    }

    @Test
    public void testSetAndGetAccountCount() {
        DashboardSummary s = new DashboardSummary();
        s.setAccountCount(10L);
        assertEquals(10L, s.getAccountCount());
    }

    @Test
    public void testSetAndGetRecentTrades() {
        DashboardSummary s = new DashboardSummary();
        List<TradeResponse> trades = new ArrayList<>();
        s.setRecentTrades(trades);
        assertSame(trades, s.getRecentTrades());
    }

    @Test
    public void testDefaultZeroValues() {
        DashboardSummary s = new DashboardSummary();
        assertEquals(0L, s.getTotalTrades());
        assertEquals(0L, s.getOpenPositions());
        assertEquals(0L, s.getInstrumentCount());
        assertEquals(0L, s.getAccountCount());
    }
}
