package com.modernizex.characterization;
import com.gs.tms.entity.Account;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class AccountEntityEdgeCasesTest {
@Test
public void testAccountWithEmptyStrings() {
Account account = new Account();
account.setAccountNumber("");
account.setClientName("");
account.setAccountType("");
account.setBaseCurrency("");
assertEquals("", account.getAccountNumber());
assertEquals("", account.getClientName());
assertEquals("", account.getAccountType());
assertEquals("", account.getBaseCurrency());
}
@Test
public void testAccountWithVeryLongStrings() {
Account account = new Account();
String longString = "A".repeat(300);
account.setAccountNumber(longString);
account.setClientName(longString);
assertEquals(longString, account.getAccountNumber());
assertEquals(longString, account.getClientName());
}
@Test
public void testAccountWithZeroId() {
Account account = new Account();
account.setId(0L);
assertEquals(0L, account.getId());
}
@Test
public void testAccountWithNegativeId() {
Account account = new Account();
account.setId(-1L);
assertEquals(-1L, account.getId());
}
@Test
public void testAccountWithMinMaxDateTime() {
Account account = new Account();
LocalDateTime min = LocalDateTime.MIN;
account.setCreatedAt(min);
assertEquals(min, account.getCreatedAt());
LocalDateTime max = LocalDateTime.MAX;
account.setCreatedAt(max);
assertEquals(max, account.getCreatedAt());
}
}