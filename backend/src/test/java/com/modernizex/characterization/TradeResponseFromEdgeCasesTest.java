package com.modernizex.characterization;

import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeResponseFromEdgeCasesTest {

    @Test
    public void testFromWithNullTradeDate() {
        Trade t = new Trade();
        t.setId(2L);
        t.setTradeRef("TRD-000002");
        t.setInstrumentId(1L);
        t.setAccountId(1L);
        t.setSide("SELL");
        t.setQuantity(new BigDecimal("50.0000"));
        t.setPrice(new BigDecimal("20.0000"));
        t.setNotional(new BigDecimal("1000.0000"));
        t.setStatus("NEW");
        t.setTradeDate(null);
        TradeResponse r = TradeResponse.from(t, "GOOG", "ACC-002");
        assertNull(r.getTradeDate());
    }

    @Test
    public void testFromWithSellSide() {
        Trade t = new Trade();
        t.setId(3L);
        t.setTradeRef("TRD-000003");
        t.setInstrumentId(2L);
        t.setAccountId(2L);
        t.setSide("SELL");
        t.setQuantity(new BigDecimal("75.0000"));
        t.setPrice(new BigDecimal("30.0000"));
        t.setNotional(new BigDecimal("2250.0000"));
        t.setStatus("NEW");
        t.setTradeDate(LocalDate.of(2024, 4, 1));
        TradeResponse r = TradeResponse.from(t, "TSLA", "ACC-003");
        assertEquals("SELL", r.getSide());
        assertEquals(new BigDecimal("2250.0000"), r.getNotional());
    }

    @Test
    public void testFromWithCancelledStatus() {
        Trade t = new Trade();
        t.setId(4L);
        t.setTradeRef("TRD-000004");
        t.setInstrumentId(3L);
        t.setAccountId(3L);
        t.setSide("BUY");
        t.setQuantity(new BigDecimal("10.0000"));
        t.setPrice(new BigDecimal("100.0000"));
        t.setNotional(new BigDecimal("1000.0000"));
        t.setStatus("CANCELLED");
        t.setTradeDate(LocalDate.of(2024, 4, 2));
        TradeResponse r = TradeResponse.from(t, "AMZN", "ACC-004");
        assertEquals("CANCELLED", r.getStatus());
    }

    @Test
    public void testFromPreservesInstrumentAndAccountIds() {
        Trade t = new Trade();
        t.setId(5L);
        t.setTradeRef("TRD-000005");
        t.setInstrumentId(99L);
        t.setAccountId(88L);
        t.setSide("BUY");
        t.setQuantity(new BigDecimal("1.0000"));
        t.setPrice(new BigDecimal("1.0000"));
        t.setNotional(new BigDecimal("1.0000"));
        t.setStatus("NEW");
        t.setTradeDate(LocalDate.of(2024, 1, 1));
        TradeResponse r = TradeResponse.from(t, "FB", "ACC-099");
        assertEquals(99L, r.getInstrumentId());
        assertEquals(88L, r.getAccountId());
    }
}
