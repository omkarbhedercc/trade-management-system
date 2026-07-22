package com.modernizex.characterization;

import com.gs.tms.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class GlobalExceptionHandlerBadRequestTest {

    @Test
    public void testHandleBadRequestReturns400() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("side must be BUY or SELL");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleBadRequestBodyContainsStatus400() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("quantity must be positive");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
    }

    @Test
    public void testHandleBadRequestBodyContainsMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("price must be non-negative");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("price must be non-negative", body.get("message"));
    }

    @Test
    public void testHandleBadRequestBodyContainsErrorPhrase() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("bad input");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Bad Request", body.get("error"));
    }
}
