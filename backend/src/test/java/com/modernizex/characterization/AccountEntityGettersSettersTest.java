package com.modernizex.characterization;
import com.gs.tms.entity.Account;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class AccountEntityGettersSettersTest {
@Test
public void testAccountIdGetterSetter() {
Account account = new Account();
assertNull(account.getId());
account.setId(100L);
assertEquals(100L, account.getId());
}
@Test
public void testAccountNumberGetterSetter() {
Account account = new Account();
assertNull(account.getAccountNumber());
account.setAccountNumber("ACC-12345");
assertEquals("ACC-12345", account.getAccountNumber());
}
@Test
public void testClientNameGetterSetter() {
Account account = new Account();
assertNull(account.getClientName());
account.setClientName("Test Client");
assertEquals("Test Client", account.getClientName());
}
@Test
public void testAccountTypeGetterSetter() {
Account account = new Account();
assertNull(account.getAccountType());
account.setAccountType("CORPORATE");
assertEquals("CORPORATE", account.getAccountType());
}
@Test
public void testBaseCurrencyGetterSetter() {
Account account = new Account();
assertNull(account.getBaseCurrency());
account.setBaseCurrency("USD");
assertEquals("USD", account.getBaseCurrency());
}
@Test
public void testCreatedAtGetterSetter() {
Account account = new Account();
assertNull(account.getCreatedAt());
LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
account.setCreatedAt(now);
assertEquals(now, account.getCreatedAt());
}
}