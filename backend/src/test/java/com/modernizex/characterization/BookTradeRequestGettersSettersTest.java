package com.modernizex.characterization;

import com.gs.tms.dto.BookTradeRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookTradeRequestGettersSettersTest {

    @Test
    public void testSetAndGetInstrumentId() {
        BookTradeRequest req = new BookTradeRequest();
        req.setInstrumentId(42L);
        assertEquals(42L, req.getInstrumentId());
    }

    @Test
    public void testSetAndGetAccountId() {
        BookTradeRequest req = new BookTradeRequest();
        req.setAccountId(99L);
        assertEquals(99L, req.getAccountId());
    }

    @Test
    public void testSetAndGetSide() {
        BookTradeRequest req = new BookTradeRequest();
        req.setSide("BUY");
        assertEquals("BUY", req.getSide());
    }

    @Test
    public void testSetAndGetQuantity() {
        BookTradeRequest req = new BookTradeRequest();
        BigDecimal qty = new BigDecimal("100.5000");
        req.setQuantity(qty);
        assertEquals(qty, req.getQuantity());
    }

    @Test
    public void testSetAndGetPrice() {
        BookTradeRequest req = new BookTradeRequest();
        BigDecimal price = new BigDecimal("250.75");
        req.setPrice(price);
        assertEquals(price, req.getPrice());
    }

    @Test
    public void testSetAndGetTradeDate() {
        BookTradeRequest req = new BookTradeRequest();
        LocalDate date = LocalDate.of(2024, 1, 15);
        req.setTradeDate(date);
        assertEquals(date, req.getTradeDate());
    }

    @Test
    public void testDefaultNullValues() {
        BookTradeRequest req = new BookTradeRequest();
        assertNull(req.getInstrumentId());
        assertNull(req.getAccountId());
        assertNull(req.getSide());
        assertNull(req.getQuantity());
        assertNull(req.getPrice());
        assertNull(req.getTradeDate());
    }
}
