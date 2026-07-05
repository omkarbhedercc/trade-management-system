package com.modernizex.characterization;
import com.gs.tms.entity.Position;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class PositionEntityGettersSettersTest {
@Test
public void testPositionIdGetterSetter() {
Position position = new Position();
assertNull(position.getId());
position.setId(10L);
assertEquals(10L, position.getId());
}
@Test
public void testAccountIdGetterSetter() {
Position position = new Position();
assertNull(position.getAccountId());
position.setAccountId(100L);
assertEquals(100L, position.getAccountId());
}
@Test
public void testInstrumentIdGetterSetter() {
Position position = new Position();
assertNull(position.getInstrumentId());
position.setInstrumentId(50L);
assertEquals(50L, position.getInstrumentId());
}
@Test
public void testNetQuantityGetterSetter() {
Position position = new Position();
assertEquals(0, position.getNetQuantity().intValue());
BigDecimal qty = new BigDecimal("1000.5000");
position.setNetQuantity(qty);
assertEquals(qty, position.getNetQuantity());
}
@Test
public void testAvgPriceGetterSetter() {
Position position = new Position();
assertEquals(0, position.getAvgPrice().intValue());
BigDecimal price = new BigDecimal("150.2500");
position.setAvgPrice(price);
assertEquals(price, position.getAvgPrice());
}
@Test
public void testMarketValueGetterSetter() {
Position position = new Position();
assertEquals(0, position.getMarketValue().intValue());
BigDecimal mv = new BigDecimal("150250.0000");
position.setMarketValue(mv);
assertEquals(mv, position.getMarketValue());
}
@Test
public void testUpdatedAtGetterSetter() {
Position position = new Position();
assertNull(position.getUpdatedAt());
LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
position.setUpdatedAt(now);
assertEquals(now, position.getUpdatedAt());
}
}