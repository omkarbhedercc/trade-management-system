package com.modernizex.characterization;
import com.gs.tms.dto.DashboardSummary;
import com.gs.tms.dto.TradeResponse;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class DashboardSummaryGettersSettersTest {
@Test
public void testTotalTradesGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertEquals(0L, summary.getTotalTrades());
summary.setTotalTrades(42L);
assertEquals(42L, summary.getTotalTrades());
}
@Test
public void testTotalNotionalGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertNull(summary.getTotalNotional());
BigDecimal notional = new BigDecimal("1000000.00");
summary.setTotalNotional(notional);
assertEquals(notional, summary.getTotalNotional());
}
@Test
public void testOpenPositionsGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertEquals(0L, summary.getOpenPositions());
summary.setOpenPositions(10L);
assertEquals(10L, summary.getOpenPositions());
}
@Test
public void testInstrumentCountGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertEquals(0L, summary.getInstrumentCount());
summary.setInstrumentCount(5L);
assertEquals(5L, summary.getInstrumentCount());
}
@Test
public void testAccountCountGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertEquals(0L, summary.getAccountCount());
summary.setAccountCount(3L);
assertEquals(3L, summary.getAccountCount());
}
@Test
public void testRecentTradesGetterSetter() {
DashboardSummary summary = new DashboardSummary();
assertNull(summary.getRecentTrades());
List<TradeResponse> trades = new ArrayList<>();
summary.setRecentTrades(trades);
assertEquals(trades, summary.getRecentTrades());
}
}