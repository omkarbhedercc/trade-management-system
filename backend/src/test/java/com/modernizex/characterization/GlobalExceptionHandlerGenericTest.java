package com.modernizex.characterization;

import com.gs.tms.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class GlobalExceptionHandlerGenericTest {

    @Test
    public void testHandleGenericReturns500() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testHandleGenericBodyContainsStatus500() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("DB connection failed");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
    }

    @Test
    public void testHandleGenericBodyContainsMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Something went wrong", body.get("message"));
    }

    @Test
    public void testHandleGenericBodyContainsErrorPhrase() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("error");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Internal Server Error", body.get("error"));
    }

    @Test
    public void testHandleGenericBodyContainsTimestamp() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("error");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("timestamp"));
    }
}
