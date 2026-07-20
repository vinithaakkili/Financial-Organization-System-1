package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoggingAspectTest {

    private LoggingAspect loggingAspect;
    private JoinPoint joinPoint;
    private Signature signature;

    @BeforeEach
    void setUp() {

        loggingAspect = new LoggingAspect();

        joinPoint = mock(JoinPoint.class);
        signature = mock(Signature.class);

        when(joinPoint.getSignature())
                .thenReturn(signature);

        when(signature.getName())
                .thenReturn("registerOrganization");
    }

    @Test
    void testLogBefore() {

        assertDoesNotThrow(() ->
                loggingAspect.logBefore(joinPoint));
    }

    @Test
    void testLogAfterReturning() {

        assertDoesNotThrow(() ->
                loggingAspect.logAfterReturning(joinPoint));
    }

    @Test
    void testLogAfterThrowing() {

        Exception exception =
                new IllegalArgumentException(
                        "Registration number already exists"
                );

        assertDoesNotThrow(() ->
                loggingAspect.logAfterThrowing(
                        joinPoint,
                        exception
                ));
    }
}