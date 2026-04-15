package com.app.quantitymeasurement.unit;

// common contract for all measurement units
public interface IMeasurable {

    String getUnitName(); // unit name

    double convertToBaseUnit(double value); // to base unit

    double convertFromBaseUnit(double baseValue); // from base unit

    String getMeasurementType(); // measurement category

    IMeasurable getUnitInstance(String unitName); // dynamic lookup

    // check arithmetic support
    default boolean supportsArithmetic() {
        return this instanceof SupportsArithmetic;
    }

    // validate arithmetic support
    default void validateOperationSupport(String operationName) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException(
                    getMeasurementType() + " does not support " + operationName);
        }
    }

    // conversion factor (for linear units)
    default double getConversionFactor() {
        return convertToBaseUnit(1.0);
    }
}