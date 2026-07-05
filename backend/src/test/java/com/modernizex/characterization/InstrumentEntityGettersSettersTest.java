package com.modernizex.characterization;
import com.gs.tms.entity.Instrument;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class InstrumentEntityGettersSettersTest {
@Test
public void testInstrumentIdGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getId());
instrument.setId(50L);
assertEquals(50L, instrument.getId());
}
@Test
public void testTickerGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getTicker());
instrument.setTicker("AAPL");
assertEquals("AAPL", instrument.getTicker());
}
@Test
public void testNameGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getName());
instrument.setName("Apple Inc.");
assertEquals("Apple Inc.", instrument.getName());
}
@Test
public void testAssetClassGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getAssetClass());
instrument.setAssetClass("EQUITY");
assertEquals("EQUITY", instrument.getAssetClass());
}
@Test
public void testCurrencyGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getCurrency());
instrument.setCurrency("USD");
assertEquals("USD", instrument.getCurrency());
}
@Test
public void testCreatedAtGetterSetter() {
Instrument instrument = new Instrument();
assertNull(instrument.getCreatedAt());
LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
instrument.setCreatedAt(now);
assertEquals(now, instrument.getCreatedAt());
}
}