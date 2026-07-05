package com.modernizex.characterization;
import com.gs.tms.dto.BookTradeRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class BookTradeRequestGettersSettersTest {
@Test
public void testInstrumentIdGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getInstrumentId());
req.setInstrumentId(50L);
assertEquals(50L, req.getInstrumentId());
}
@Test
public void testAccountIdGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getAccountId());
req.setAccountId(100L);
assertEquals(100L, req.getAccountId());
}
@Test
public void testSideGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getSide());
req.setSide("BUY");
assertEquals("BUY", req.getSide());
}
@Test
public void testQuantityGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getQuantity());
BigDecimal qty = new BigDecimal("100.0000");
req.setQuantity(qty);
assertEquals(qty, req.getQuantity());
}
@Test
public void testPriceGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getPrice());
BigDecimal price = new BigDecimal("150.25");
req.setPrice(price);
assertEquals(price, req.getPrice());
}
@Test
public void testTradeDateGetterSetter() {
BookTradeRequest req = new BookTradeRequest();
assertNull(req.getTradeDate());
LocalDate date = LocalDate.of(2024, 1, 15);
req.setTradeDate(date);
assertEquals(date, req.getTradeDate());
}
}