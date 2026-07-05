package com.modernizex.characterization;
import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class TradeResponseFromEdgeCasesTest {
@Test
public void testTradeResponseFromWithZeroQuantity() {
Trade trade = new Trade();
trade.setId(10L);
trade.setTradeRef("TRD-000010");
trade.setInstrumentId(1L);
trade.setAccountId(1L);
trade.setSide("BUY");
trade.setQuantity(BigDecimal.ZERO);
trade.setPrice(new BigDecimal("100.00"));
trade.setNotional(BigDecimal.ZERO);
trade.setStatus("NEW");
trade.setTradeDate(LocalDate.of(2024, 1, 1));
TradeResponse response = TradeResponse.from(trade, "TEST", "ACC-001");
assertEquals(BigDecimal.ZERO, response.getQuantity());
assertEquals(BigDecimal.ZERO, response.getNotional());
}
@Test
public void testTradeResponseFromWithZeroPrice() {
Trade trade = new Trade();
trade.setId(11L);
trade.setTradeRef("TRD-000011");
trade.setInstrumentId(2L);
trade.setAccountId(2L);
trade.setSide("SELL");
trade.setQuantity(new BigDecimal("100.00"));
trade.setPrice(BigDecimal.ZERO);
trade.setNotional(BigDecimal.ZERO);
trade.setStatus("NEW");
trade.setTradeDate(LocalDate.of(2024, 1, 2));
TradeResponse response = TradeResponse.from(trade, "TEST2", "ACC-002");
assertEquals(BigDecimal.ZERO, response.getPrice());
}
@Test
public void testTradeResponseFromWithEmptyStrings() {
Trade trade = new Trade();
trade.setId(12L);
trade.setTradeRef("");
trade.setInstrumentId(3L);
trade.setAccountId(3L);
trade.setSide("");
trade.setQuantity(new BigDecimal("1.00"));
trade.setPrice(new BigDecimal("1.00"));
trade.setNotional(new BigDecimal("1.00"));
trade.setStatus("");
trade.setTradeDate(LocalDate.of(2024, 1, 3));
TradeResponse response = TradeResponse.from(trade, "", "");
assertEquals("", response.getTradeRef());
assertEquals("", response.getSide());
assertEquals("", response.getStatus());
assertEquals("", response.getInstrumentTicker());
assertEquals("", response.getAccountNumber());
}
}