package com.modernizex.characterization;
import com.gs.tms.entity.Position;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
public class PositionEntityEdgeCasesTest {
@Test
public void testPositionWithZeroQuantity() {
Position position = new Position();
position.setNetQuantity(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, position.getNetQuantity());
}
@Test
public void testPositionWithNegativeQuantity() {
Position position = new Position();
BigDecimal negQty = new BigDecimal("-1000.0000");
position.setNetQuantity(negQty);
assertEquals(negQty, position.getNetQuantity());
}
@Test
public void testPositionWithVeryLargeQuantity() {
Position position = new Position();
BigDecimal largeQty = new BigDecimal("999999999999.9999");
position.setNetQuantity(largeQty);
assertEquals(largeQty, position.getNetQuantity());
}
@Test
public void testPositionWithZeroPrice() {
Position position = new Position();
position.setAvgPrice(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, position.getAvgPrice());
}
@Test
public void testPositionWithNegativeMarketValue() {
Position position = new Position();
BigDecimal negMV = new BigDecimal("-50000.0000");
position.setMarketValue(negMV);
assertEquals(negMV, position.getMarketValue());
}
@Test
public void testPositionWithZeroIds() {
Position position = new Position();
position.setId(0L);
position.setAccountId(0L);
position.setInstrumentId(0L);
assertEquals(0L, position.getId());
assertEquals(0L, position.getAccountId());
assertEquals(0L, position.getInstrumentId());
}
}