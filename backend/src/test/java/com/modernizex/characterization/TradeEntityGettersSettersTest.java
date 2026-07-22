package com.modernizex.characterization;

import com.gs.tms.entity.Trade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TradeEntityGettersSettersTest {

    @Test
    public void testSetAndGetId() {
        Trade t = new Trade();
        t.setId(5L);
        assertEquals(5L, t.getId());
    }

    @Test
    public void testSetAndGetTradeRef() {
        Trade t = new Trade();
        t.setTradeRef("TRD-000042");
        assertEquals("TRD-000042", t.getTradeRef());
    }

    @Test
    public void testSetAndGetInstrumentId() {
        Trade t = new Trade();
        t.setInstrumentId(7L);
        assertEquals(7L, t.getInstrumentId());
    }

    @Test
    public void testSetAndGetAccountId() {
        Trade t = new Trade();
        t.setAccountId(3L);
        assertEquals(3L, t.getAccountId());
    }

    @Test
    public void testSetAndGetSide() {
        Trade t = new Trade();
        t.setSide("SELL");
        assertEquals("SELL", t.getSide());
    }

    @Test
    public void testSetAndGetQuantityPriceNotional() {
        Trade t = new Trade();
        t.setQuantity(new BigDecimal("200.0000"));
        t.setPrice(new BigDecimal("10.5000"));
        t.setNotional(new BigDecimal("2100.0000"));
        assertEquals(new BigDecimal("200.0000"), t.getQuantity());
        assertEquals(new BigDecimal("10.5000"), t.getPrice());
        assertEquals(new BigDecimal("2100.0000"), t.getNotional());
    }

    @Test
    public void testSetAndGetStatus() {
        Trade t = new Trade();
        t.setStatus("CANCELLED");
        assertEquals("CANCELLED", t.getStatus());
    }

    @Test
    public void testSetAndGetTradeDate() {
        Trade t = new Trade();
        LocalDate d = LocalDate.of(2024, 6, 15);
        t.setTradeDate(d);
        assertEquals(d, t.getTradeDate());
    }

    @Test
    public void testSetAndGetBookedAt() {
        Trade t = new Trade();
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 9, 30, 0);
        t.setBookedAt(dt);
        assertEquals(dt, t.getBookedAt());
    }

    @Test
    public void testDefaultNullValues() {
        Trade t = new Trade();
        assertNull(t.getId());
        assertNull(t.getTradeRef());
        assertNull(t.getStatus());
    }
}
