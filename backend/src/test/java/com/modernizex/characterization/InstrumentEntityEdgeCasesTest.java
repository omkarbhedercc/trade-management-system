package com.modernizex.characterization;
import com.gs.tms.entity.Instrument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class InstrumentEntityEdgeCasesTest {
@Test
public void testInstrumentWithEmptyStrings() {
Instrument instrument = new Instrument();
instrument.setTicker("");
instrument.setName("");
instrument.setAssetClass("");
instrument.setCurrency("");
assertEquals("", instrument.getTicker());
assertEquals("", instrument.getName());
assertEquals("", instrument.getAssetClass());
assertEquals("", instrument.getCurrency());
}
@Test
public void testInstrumentWithVeryLongTicker() {
Instrument instrument = new Instrument();
String longTicker = "ABCDEFGHIJ".repeat(10);
instrument.setTicker(longTicker);
assertEquals(longTicker, instrument.getTicker());
}
@Test
public void testInstrumentWithZeroId() {
Instrument instrument = new Instrument();
instrument.setId(0L);
assertEquals(0L, instrument.getId());
}
@Test
public void testInstrumentWithNegativeId() {
Instrument instrument = new Instrument();
instrument.setId(-999L);
assertEquals(-999L, instrument.getId());
}
@Test
public void testInstrumentWithSpecialCharacters() {
Instrument instrument = new Instrument();
instrument.setTicker("@#$%");
instrument.setName("Test & Co.");
assertEquals("@#$%", instrument.getTicker());
assertEquals("Test & Co.", instrument.getName());
}
}