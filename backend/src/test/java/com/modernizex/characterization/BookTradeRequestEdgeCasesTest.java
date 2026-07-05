package com.modernizex.characterization;
import com.gs.tms.dto.BookTradeRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class BookTradeRequestEdgeCasesTest {
@Test
public void testBookTradeRequestWithZeroQuantity() {
BookTradeRequest req = new BookTradeRequest();
req.setQuantity(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, req.getQuantity());
}
@Test
public void testBookTradeRequestWithNegativeQuantity() {
BookTradeRequest req = new BookTradeRequest();
BigDecimal negQty = new BigDecimal("-100.00");
req.setQuantity(negQty);
assertEquals(negQty, req.getQuantity());
}
@Test
public void testBookTradeRequestWithZeroPrice() {
BookTradeRequest req = new BookTradeRequest();
req.setPrice(BigDecimal.ZERO);
assertEquals(BigDecimal.ZERO, req.getPrice());
}
@Test
public void testBookTradeRequestWithNegativePrice() {
BookTradeRequest req = new BookTradeRequest();
BigDecimal negPrice = new BigDecimal("-50.00");
req.setPrice(negPrice);
assertEquals(negPrice, req.getPrice());
}
@Test
public void testBookTradeRequestWithEmptySide() {
BookTradeRequest req = new BookTradeRequest();
req.setSide("");
assertEquals("", req.getSide());
}
@Test
public void testBookTradeRequestWithLowercaseSide() {
BookTradeRequest req = new BookTradeRequest();
req.setSide("buy");
assertEquals("buy", req.getSide());
}
@Test
public void testBookTradeRequestWithFutureTrade() {
BookTradeRequest req = new BookTradeRequest();
LocalDate future = LocalDate.of(2099, 12, 31);
req.setTradeDate(future);
assertEquals(future, req.getTradeDate());
}
}