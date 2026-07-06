package com.modernizex.characterization;

import com.gs.tms.dto.DashboardSummary;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

public class DashboardSummaryEdgeCasesTest {

    @Test
    public void testSetTotalNotionalToZero() {
        DashboardSummary s = new DashboardSummary();
        s.setTotalNotional(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, s.getTotalNotional());
    }

    @Test
    public void testSetTotalNotionalToNull() {
        DashboardSummary s = new DashboardSummary();
        s.setTotalNotional(null);
        assertNull(s.getTotalNotional());
    }

    @Test
    public void testSetRecentTradesToEmptyList() {
        DashboardSummary s = new DashboardSummary();
        s.setRecentTrades(Collections.emptyList());
        assertNotNull(s.getRecentTrades());
        assertEquals(0, s.getRecentTrades().size());
    }

    @Test
    public void testSetRecentTradesToNull() {
        DashboardSummary s = new DashboardSummary();
        s.setRecentTrades(null);
        assertNull(s.getRecentTrades());
    }

    @Test
    public void testSetTotalTradesToMaxLong() {
        DashboardSummary s = new DashboardSummary();
        s.setTotalTrades(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, s.getTotalTrades());
    }

    @Test
    public void testSetOpenPositionsToZero() {
        DashboardSummary s = new DashboardSummary();
        s.setOpenPositions(0L);
        assertEquals(0L, s.getOpenPositions());
    }
}
