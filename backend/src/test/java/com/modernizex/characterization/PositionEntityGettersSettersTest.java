package com.modernizex.characterization;

import com.gs.tms.entity.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PositionEntityGettersSettersTest {

    @Test
    public void testSetAndGetId() {
        Position p = new Position();
        p.setId(1L);
        assertEquals(1L, p.getId());
    }

    @Test
    public void testSetAndGetAccountId() {
        Position p = new Position();
        p.setAccountId(5L);
        assertEquals(5L, p.getAccountId());
    }

    @Test
    public void testSetAndGetInstrumentId() {
        Position p = new Position();
        p.setInstrumentId(8L);
        assertEquals(8L, p.getInstrumentId());
    }

    @Test
    public void testDefaultNetQuantityIsZero() {
        Position p = new Position();
        assertEquals(BigDecimal.ZERO, p.getNetQuantity());
    }

    @Test
    public void testDefaultAvgPriceIsZero() {
        Position p = new Position();
        assertEquals(BigDecimal.ZERO, p.getAvgPrice());
    }

    @Test
    public void testDefaultMarketValueIsZero() {
        Position p = new Position();
        assertEquals(BigDecimal.ZERO, p.getMarketValue());
    }

    @Test
    public void testSetAndGetNetQuantity() {
        Position p = new Position();
        BigDecimal qty = new BigDecimal("500.0000");
        p.setNetQuantity(qty);
        assertEquals(qty, p.getNetQuantity());
    }

    @Test
    public void testSetAndGetAvgPrice() {
        Position p = new Position();
        BigDecimal avg = new BigDecimal("123.4567");
        p.setAvgPrice(avg);
        assertEquals(avg, p.getAvgPrice());
    }

    @Test
    public void testSetAndGetMarketValue() {
        Position p = new Position();
        BigDecimal mv = new BigDecimal("61728.3500");
        p.setMarketValue(mv);
        assertEquals(mv, p.getMarketValue());
    }

    @Test
    public void testSetAndGetUpdatedAt() {
        Position p = new Position();
        LocalDateTime dt = LocalDateTime.of(2024, 5, 10, 14, 30, 0);
        p.setUpdatedAt(dt);
        assertEquals(dt, p.getUpdatedAt());
    }
}
