package com.app.quantitymeasurement.exception;

// custom runtime exception for quantity operations
public class QuantityMeasurementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // constructor with message
    public QuantityMeasurementException(String message) {
        super(message);
    }

    // constructor with message and cause
    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}