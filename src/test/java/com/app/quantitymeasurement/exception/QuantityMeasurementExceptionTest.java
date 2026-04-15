package com.app.quantitymeasurement.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for QuantityMeasurementException
class QuantityMeasurementExceptionTest {

    // ===================== CLASS =====================

    @Test
    void testExtendsRuntimeException() {
        assertTrue(new QuantityMeasurementException("msg") instanceof RuntimeException);
    }

    @Test
    void testIsUnchecked() {
        assertTrue(RuntimeException.class.isAssignableFrom(
            QuantityMeasurementException.class));
        assertFalse(
            QuantityMeasurementException.class.getSuperclass().equals(Exception.class));
    }

    // ===================== MESSAGE CONSTRUCTOR =====================

    @Test
    void testConstructor_Message() {
        QuantityMeasurementException ex =
            new QuantityMeasurementException("Invalid unit provided");

        assertEquals("Invalid unit provided", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testConstructor_EmptyMessage() {
        QuantityMeasurementException ex = new QuantityMeasurementException("");
        assertEquals("", ex.getMessage());
    }

    // ===================== MESSAGE + CAUSE =====================

    @Test
    void testConstructor_MessageAndCause() {
        Throwable cause = new IllegalArgumentException("root cause");

        QuantityMeasurementException ex =
            new QuantityMeasurementException("wrapper message", cause);

        assertEquals("wrapper message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testConstructor_CauseMessageAccessible() {
        Throwable cause = new ArithmeticException("division by zero");

        QuantityMeasurementException ex =
            new QuantityMeasurementException("arithmetic error", cause);

        assertEquals("division by zero", ex.getCause().getMessage());
    }

    // ===================== THROW / CATCH =====================

    @Test
    void testThrowAndCatch_MessageOnly() {
        QuantityMeasurementException caught = assertThrows(
            QuantityMeasurementException.class,
            () -> { throw new QuantityMeasurementException("test error"); }
        );

        assertEquals("test error", caught.getMessage());
    }

    @Test
    void testThrowAndCatch_WithCause() {
        IllegalArgumentException root = new IllegalArgumentException("bad arg");

        QuantityMeasurementException caught = assertThrows(
            QuantityMeasurementException.class,
            () -> { throw new QuantityMeasurementException("wrapper", root); }
        );

        assertSame(root, caught.getCause());
    }

    @Test
    void testCaughtAsRuntimeException() {
        assertThrows(
            RuntimeException.class,
            () -> { throw new QuantityMeasurementException("runtime"); }
        );
    }
}