package com.modernizex.characterization;
import com.gs.tms.dto.DashboardSummary;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
public class DashboardSummaryEdgeCasesTest {
@Test
public void testDashboardSummaryWithZeroCounts() {
DashboardSummary summary = new DashboardSummary();
summary.setTotalTrades(0L);
summary.setOpenPositions(0L);
summary.setInstrumentCount(0L);
summary.setAccountCount(0L);
assertEquals(0L, summary.getTotalTrades());
assertEquals(0L, summary.getOpenPositions());
assertEquals(0L, summary.getInstrumentCount());
assertEquals(0L, summary.getAccountCount());
}
@Test
public void testDashboardSummaryWithNegativeCounts() {
DashboardSummary summary = new DashboardSummary();
summary.setTotalTrades(-1L);
summary.setOpenPositions(-5L);
assertEquals(-1L, summary.getTotalTrades());
assertEquals(-5L, summary.getOpenPositions());
}
@Test
public void testDashboardSummaryWithZeroNotional() {
DashboardSummary summary = new DashboardSummary();
summary.setTotalNotional(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, summary.getTotalNotional());
}
@Test
public void testDashboardSummaryWithNegativeNotional() {
DashboardSummary summary = new DashboardSummary();
BigDecimal negNotional = new BigDecimal("-1000.00");
summary.setTotalNotional(negNotional);
assertEquals(negNotional, summary.getTotalNotional());
}
@Test
public void testDashboardSummaryWithEmptyRecentTrades() {
DashboardSummary summary = new DashboardSummary();
summary.setRecentTrades(new ArrayList<>());
assertNotNull(summary.getRecentTrades());
assertEquals(0, summary.getRecentTrades().size());
}
}