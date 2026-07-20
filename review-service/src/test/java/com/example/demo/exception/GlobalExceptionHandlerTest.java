package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        handler = new GlobalExceptionHandler();

        when(request.getRequestURI())
                .thenReturn("/organizations/1");
    }

    @Test
    void testHandleOrganizationNotFoundException() {

        ReviewNotFoundException exception =
                new ReviewNotFoundException(
                        "Organization not found"
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleNotFound(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                404,
                response.getBody().getStatus()
        );

        assertEquals(
                "Not Found",
                response.getBody().getError()
        );

        assertEquals(
                "Organization not found",
                response.getBody().getMessage()
        );

        assertEquals(
                "/organizations/1",
                response.getBody().getPath()
        );

        assertNotNull(
                response.getBody().getTimestamp()
        );
    }

    @Test
    void testHandleIllegalArgumentException() {

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Invalid organization data"
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleBadRequest(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                400,
                response.getBody().getStatus()
        );

        assertEquals(
                "Bad Request",
                response.getBody().getError()
        );

        assertEquals(
                "Invalid organization data",
                response.getBody().getMessage()
        );

        assertEquals(
                "/organizations/1",
                response.getBody().getPath()
        );
    }

    @Test
    void testHandleGenericException() {

        Exception exception =
                new RuntimeException(
                        "Unexpected error"
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                500,
                response.getBody().getStatus()
        );

        assertEquals(
                "Internal Server Error",
                response.getBody().getError()
        );

        assertEquals(
                "Unexpected error",
                response.getBody().getMessage()
        );

        assertEquals(
                "/organizations/1",
                response.getBody().getPath()
        );
    }
}