package com.modernizex.characterization;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class TradeEntityGettersSettersTest {
@Test
public void testTradeIdGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getId());
trade.setId(1L);
assertEquals(1L, trade.getId());
}
@Test
public void testTradeRefGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getTradeRef());
trade.setTradeRef("TRD-000001");
assertEquals("TRD-000001", trade.getTradeRef());
}
@Test
public void testInstrumentIdGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getInstrumentId());
trade.setInstrumentId(50L);
assertEquals(50L, trade.getInstrumentId());
}
@Test
public void testAccountIdGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getAccountId());
trade.setAccountId(100L);
assertEquals(100L, trade.getAccountId());
}
@Test
public void testSideGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getSide());
trade.setSide("BUY");
assertEquals("BUY", trade.getSide());
}
@Test
public void testQuantityGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getQuantity());
BigDecimal qty = new BigDecimal("100.0000");
trade.setQuantity(qty);
assertEquals(qty, trade.getQuantity());
}
@Test
public void testPriceGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getPrice());
BigDecimal price = new BigDecimal("150.2500");
trade.setPrice(price);
assertEquals(price, trade.getPrice());
}
@Test
public void testNotionalGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getNotional());
BigDecimal notional = new BigDecimal("15025.0000");
trade.setNotional(notional);
assertEquals(notional, trade.getNotional());
}
@Test
public void testStatusGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getStatus());
trade.setStatus("NEW");
assertEquals("NEW", trade.getStatus());
}
@Test
public void testTradeDateGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getTradeDate());
LocalDate date = LocalDate.of(2024, 1, 15);
trade.setTradeDate(date);
assertEquals(date, trade.getTradeDate());
}
@Test
public void testBookedAtGetterSetter() {
Trade trade = new Trade();
assertNull(trade.getBookedAt());
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
trade.setBookedAt(dt);
assertEquals(dt, trade.getBookedAt());
}
}