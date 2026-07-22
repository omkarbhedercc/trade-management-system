package com.modernizex.characterization;

import com.gs.tms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testMessageIsPreserved() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Account not found: 42");
        assertEquals("Account not found: 42", ex.getMessage());
    }

    @Test
    public void testIsRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Trade not found: 99");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void testNullMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException(null);
        assertNull(ex.getMessage());
    }

    @Test
    public void testEmptyMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("");
        assertEquals("", ex.getMessage());
    }

    @Test
    public void testCanBeThrown() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Instrument not found: 7");
        });
    }
}
