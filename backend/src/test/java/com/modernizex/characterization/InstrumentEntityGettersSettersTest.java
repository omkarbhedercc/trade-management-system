package com.modernizex.characterization;

import com.gs.tms.entity.Instrument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

public class InstrumentEntityGettersSettersTest {

    @Test
    public void testSetAndGetId() {
        Instrument i = new Instrument();
        i.setId(10L);
        assertEquals(10L, i.getId());
    }

    @Test
    public void testSetAndGetTicker() {
        Instrument i = new Instrument();
        i.setTicker("AAPL");
        assertEquals("AAPL", i.getTicker());
    }

    @Test
    public void testSetAndGetName() {
        Instrument i = new Instrument();
        i.setName("Apple Inc.");
        assertEquals("Apple Inc.", i.getName());
    }

    @Test
    public void testSetAndGetAssetClass() {
        Instrument i = new Instrument();
        i.setAssetClass("EQUITY");
        assertEquals("EQUITY", i.getAssetClass());
    }

    @Test
    public void testSetAndGetCurrency() {
        Instrument i = new Instrument();
        i.setCurrency("USD");
        assertEquals("USD", i.getCurrency());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        Instrument i = new Instrument();
        LocalDateTime dt = LocalDateTime.of(2024, 2, 20, 8, 0, 0);
        i.setCreatedAt(dt);
        assertEquals(dt, i.getCreatedAt());
    }

    @Test
    public void testDefaultNullValues() {
        Instrument i = new Instrument();
        assertNull(i.getId());
        assertNull(i.getTicker());
        assertNull(i.getName());
        assertNull(i.getAssetClass());
        assertNull(i.getCurrency());
        assertNull(i.getCreatedAt());
    }
}
