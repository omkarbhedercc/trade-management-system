package com.modernizex.characterization;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class TradeEntityEdgeCasesTest {
@Test
public void testTradeWithEmptyStrings() {
Trade trade = new Trade();
trade.setTradeRef("");
trade.setSide("");
trade.setStatus("");
assertEquals("", trade.getTradeRef());
assertEquals("", trade.getSide());
assertEquals("", trade.getStatus());
}
@Test
public void testTradeWithZeroValues() {
Trade trade = new Trade();
trade.setQuantity(BigDecimal.ZERO);
trade.setPrice(BigDecimal.ZERO);
trade.setNotional(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, trade.getQuantity());
assertEquals(BigDecimal.ZERO, trade.getPrice());
assertEquals(BigDecimal.ZERO, trade.getNotional());
}
@Test
public void testTradeWithNegativeValues() {
Trade trade = new Trade();
BigDecimal negQty = new BigDecimal("-100.0000");
BigDecimal negPrice = new BigDecimal("-50.0000");
BigDecimal negNotional = new BigDecimal("-5000.0000");
trade.setQuantity(negQty);
trade.setPrice(negPrice);
trade.setNotional(negNotional);
assertEquals(negQty, trade.getQuantity());
assertEquals(negPrice, trade.getPrice());
assertEquals(negNotional, trade.getNotional());
}
@Test
public void testTradeWithVeryOldDate() {
Trade trade = new Trade();
LocalDate oldDate = LocalDate.of(1900, 1, 1);
trade.setTradeDate(oldDate);
assertEquals(oldDate, trade.getTradeDate());
}
@Test
public void testTradeWithFutureDate() {
Trade trade = new Trade();
LocalDate futureDate = LocalDate.of(2099, 12, 31);
trade.setTradeDate(futureDate);
assertEquals(futureDate, trade.getTradeDate());
}
@Test
public void testTradeWithZeroIds() {
Trade trade = new Trade();
trade.setId(0L);
trade.setInstrumentId(0L);
trade.setAccountId(0L);
assertEquals(0L, trade.getId());
assertEquals(0L, trade.getInstrumentId());
assertEquals(0L, trade.getAccountId());
}
}