package com.modernizex.characterization;

import com.gs.tms.entity.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

public class PositionEntityEdgeCasesTest {

    @Test
    public void testSetNetQuantityToNegative() {
        Position p = new Position();
        BigDecimal neg = new BigDecimal("-100.0000");
        p.setNetQuantity(neg);
        assertEquals(neg, p.getNetQuantity());
    }

    @Test
    public void testSetAvgPriceToZero() {
        Position p = new Position();
        p.setAvgPrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, p.getAvgPrice());
    }

    @Test
    public void testSetMarketValueToNegative() {
        Position p = new Position();
        BigDecimal neg = new BigDecimal("-5000.0000");
        p.setMarketValue(neg);
        assertEquals(neg, p.getMarketValue());
    }

    @Test
    public void testSetUpdatedAtToNull() {
        Position p = new Position();
        p.setUpdatedAt(null);
        assertNull(p.getUpdatedAt());
    }

    @Test
    public void testSetIdToNull() {
        Position p = new Position();
        p.setId(null);
        assertNull(p.getId());
    }

    @Test
    public void testSetNetQuantityToVeryLargeValue() {
        Position p = new Position();
        BigDecimal large = new BigDecimal("99999999999999.9999");
        p.setNetQuantity(large);
        assertEquals(large, p.getNetQuantity());
    }
}
