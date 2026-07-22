package com.modernizex.characterization;

import com.gs.tms.exception.GlobalExceptionHandler;
import com.gs.tms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class GlobalExceptionHandlerNotFoundTest {

    @Test
    public void testHandleNotFoundReturns404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Account not found: 1");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleNotFoundBodyContainsStatus404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Trade not found: 5");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
    }

    @Test
    public void testHandleNotFoundBodyContainsMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Instrument not found: 3");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Instrument not found: 3", body.get("message"));
    }

    @Test
    public void testHandleNotFoundBodyContainsErrorPhrase() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Not Found", body.get("error"));
    }

    @Test
    public void testHandleNotFoundBodyContainsTimestamp() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("timestamp"));
    }
}
