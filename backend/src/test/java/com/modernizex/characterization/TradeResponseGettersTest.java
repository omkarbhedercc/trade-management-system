package com.modernizex.characterization;
import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class TradeResponseGettersTest {
@Test
public void testTradeResponseFromMethod() {
Trade trade = new Trade();
trade.setId(1L);
trade.setTradeRef("TRD-000001");
trade.setInstrumentId(50L);
trade.setAccountId(100L);
trade.setSide("BUY");
trade.setQuantity(new BigDecimal("100.0000"));
trade.setPrice(new BigDecimal("150.2500"));
trade.setNotional(new BigDecimal("15025.0000"));
trade.setStatus("NEW");
trade.setTradeDate(LocalDate.of(2024, 1, 15));
trade.setBookedAt(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
TradeResponse response = TradeResponse.from(trade, "AAPL", "ACC-12345");
assertEquals(1L, response.getId());
assertEquals("TRD-000001", response.getTradeRef());
assertEquals(50L, response.getInstrumentId());
assertEquals("AAPL", response.getInstrumentTicker());
assertEquals(100L, response.getAccountId());
assertEquals("ACC-12345", response.getAccountNumber());
assertEquals("BUY", response.getSide());
assertEquals(new BigDecimal("100.0000"), response.getQuantity());
assertEquals(new BigDecimal("150.2500"), response.getPrice());
assertEquals(new BigDecimal("15025.0000"), response.getNotional());
assertEquals("NEW", response.getStatus());
assertEquals(LocalDate.of(2024, 1, 15), response.getTradeDate());
assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), response.getBookedAt());
}
@Test
public void testTradeResponseFromMethodWithNullTicker() {
Trade trade = new Trade();
trade.setId(2L);
trade.setTradeRef("TRD-000002");
trade.setInstrumentId(51L);
trade.setAccountId(101L);
trade.setSide("SELL");
trade.setQuantity(new BigDecimal("50.0000"));
trade.setPrice(new BigDecimal("200.0000"));
trade.setNotional(new BigDecimal("10000.0000"));
trade.setStatus("CANCELLED");
trade.setTradeDate(LocalDate.of(2024, 1, 16));
trade.setBookedAt(LocalDateTime.of(2024, 1, 16, 11, 0, 0));
TradeResponse response = TradeResponse.from(trade, null, null);
assertEquals(2L, response.getId());
assertNull(response.getInstrumentTicker());
assertNull(response.getAccountNumber());
}
}