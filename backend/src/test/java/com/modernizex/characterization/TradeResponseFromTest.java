package com.modernizex.characterization;

import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TradeResponseFromTest {

    private Trade buildTrade() {
        Trade t = new Trade();
        t.setId(1L);
        t.setTradeRef("TRD-000001");
        t.setInstrumentId(10L);
        t.setAccountId(20L);
        t.setSide("BUY");
        t.setQuantity(new BigDecimal("100.0000"));
        t.setPrice(new BigDecimal("50.0000"));
        t.setNotional(new BigDecimal("5000.0000"));
        t.setStatus("NEW");
        t.setTradeDate(LocalDate.of(2024, 3, 1));
        t.setBookedAt(LocalDateTime.of(2024, 3, 1, 10, 0, 0));
        return t;
    }

    @Test
    public void testFromMapsAllFields() {
        Trade t = buildTrade();
        TradeResponse r = TradeResponse.from(t, "AAPL", "ACC-001");
        assertEquals(1L, r.getId());
        assertEquals("TRD-000001", r.getTradeRef());
        assertEquals(10L, r.getInstrumentId());
        assertEquals("AAPL", r.getInstrumentTicker());
        assertEquals(20L, r.getAccountId());
        assertEquals("ACC-001", r.getAccountNumber());
        assertEquals("BUY", r.getSide());
        assertEquals(new BigDecimal("100.0000"), r.getQuantity());
        assertEquals(new BigDecimal("50.0000"), r.getPrice());
        assertEquals(new BigDecimal("5000.0000"), r.getNotional());
        assertEquals("NEW", r.getStatus());
        assertEquals(LocalDate.of(2024, 3, 1), r.getTradeDate());
        assertEquals(LocalDateTime.of(2024, 3, 1, 10, 0, 0), r.getBookedAt());
    }

    @Test
    public void testFromWithNullTickerAndAccountNumber() {
        Trade t = buildTrade();
        TradeResponse r = TradeResponse.from(t, null, null);
        assertNull(r.getInstrumentTicker());
        assertNull(r.getAccountNumber());
        assertEquals(1L, r.getId());
    }

    @Test
    public void testFromWithNullBookedAt() {
        Trade t = buildTrade();
        t.setBookedAt(null);
        TradeResponse r = TradeResponse.from(t, "MSFT", "ACC-002");
        assertNull(r.getBookedAt());
    }
}
