package com.modernizex.characterization;

import com.gs.tms.entity.Account;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

public class AccountEntityGettersSettersTest {

    @Test
    public void testSetAndGetId() {
        Account a = new Account();
        a.setId(1L);
        assertEquals(1L, a.getId());
    }

    @Test
    public void testSetAndGetAccountNumber() {
        Account a = new Account();
        a.setAccountNumber("ACC-12345");
        assertEquals("ACC-12345", a.getAccountNumber());
    }

    @Test
    public void testSetAndGetClientName() {
        Account a = new Account();
        a.setClientName("Goldman Sachs");
        assertEquals("Goldman Sachs", a.getClientName());
    }

    @Test
    public void testSetAndGetAccountType() {
        Account a = new Account();
        a.setAccountType("INSTITUTIONAL");
        assertEquals("INSTITUTIONAL", a.getAccountType());
    }

    @Test
    public void testSetAndGetBaseCurrency() {
        Account a = new Account();
        a.setBaseCurrency("USD");
        assertEquals("USD", a.getBaseCurrency());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        Account a = new Account();
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        a.setCreatedAt(now);
        assertEquals(now, a.getCreatedAt());
    }

    @Test
    public void testDefaultNullValues() {
        Account a = new Account();
        assertNull(a.getId());
        assertNull(a.getAccountNumber());
        assertNull(a.getClientName());
        assertNull(a.getAccountType());
        assertNull(a.getBaseCurrency());
        assertNull(a.getCreatedAt());
    }
}
