package com.modernizex.characterization;

import com.gs.tms.dto.BookTradeRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

public class BookTradeRequestEdgeCasesTest {

    @Test
    public void testSetQuantityToZero() {
        BookTradeRequest req = new BookTradeRequest();
        req.setQuantity(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, req.getQuantity());
    }

    @Test
    public void testSetPriceToZero() {
        BookTradeRequest req = new BookTradeRequest();
        req.setPrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, req.getPrice());
    }

    @Test
    public void testSetSideToLowercase() {
        BookTradeRequest req = new BookTradeRequest();
        req.setSide("buy");
        assertEquals("buy", req.getSide());
    }

    @Test
    public void testSetInstrumentIdToNull() {
        BookTradeRequest req = new BookTradeRequest();
        req.setInstrumentId(null);
        assertNull(req.getInstrumentId());
    }

    @Test
    public void testSetAccountIdToNull() {
        BookTradeRequest req = new BookTradeRequest();
        req.setAccountId(null);
        assertNull(req.getAccountId());
    }

    @Test
    public void testSetTradeDateToNull() {
        BookTradeRequest req = new BookTradeRequest();
        req.setTradeDate(null);
        assertNull(req.getTradeDate());
    }
}
