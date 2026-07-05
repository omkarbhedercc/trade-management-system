package com.modernizex.characterization;
import com.gs.tms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ResourceNotFoundExceptionTest {
@Test
public void testResourceNotFoundExceptionMessage() {
ResourceNotFoundException ex = new ResourceNotFoundException("Account not found: 123");
assertEquals("Account not found: 123", ex.getMessage());
}
@Test
public void testResourceNotFoundExceptionIsRuntimeException() {
ResourceNotFoundException ex = new ResourceNotFoundException("Test");
assertTrue(ex instanceof RuntimeException);
}
@Test
public void testResourceNotFoundExceptionWithEmptyMessage() {
ResourceNotFoundException ex = new ResourceNotFoundException("");
assertEquals("", ex.getMessage());
}
@Test
public void testResourceNotFoundExceptionWithNullMessage() {
ResourceNotFoundException ex = new ResourceNotFoundException(null);
assertNull(ex.getMessage());
}
}