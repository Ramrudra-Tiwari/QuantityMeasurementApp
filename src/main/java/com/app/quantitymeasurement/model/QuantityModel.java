package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * Internal model representing a value with its unit.
 * Used in the service layer for processing operations.
 *
 * Separate from DTO (API) and Entity (database).
 *
 * @param <U> unit type implementing IMeasurable
 */
public class QuantityModel<U extends IMeasurable> {

    private final Double value;
    private final U unit;

    /**
     * Creates a QuantityModel with value and unit.
     *
     * @throws IllegalArgumentException if unit is null or value is not finite
     */
    public QuantityModel(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit  = unit;
    }

    /** @return quantity value */
    public double getValue() {
        return value;
    }

    /** @return unit of the quantity */
    public U getUnit() {
        return unit;
    }

    /** @return formatted string like "5 FEET" */
    @Override
    public String toString() {
        return String.format("%s %s",
            Double.toString(value).replaceAll("\\.0+$", ""),
            unit.getUnitName());
    }
}