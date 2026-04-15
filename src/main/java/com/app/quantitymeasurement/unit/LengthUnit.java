package com.app.quantitymeasurement.unit;

// length units (base unit: INCHES)
public enum LengthUnit implements IMeasurable, SupportsArithmetic {

    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(1 / 2.54);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // to base unit (INCHES)
    @Override
    public double convertToBaseUnit(double value) {
        return Math.round(value * conversionFactor * 1_000_000.0) / 1_000_000.0;
    }

    // from base unit
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return Math.round(baseValue / conversionFactor * 1_000_000.0) / 1_000_000.0;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return this.getClass().getSimpleName();
    }

    // find unit by name
    @Override
    public IMeasurable getUnitInstance(String unitName) {
        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) return unit;
        }
        throw new IllegalArgumentException("Invalid length unit: " + unitName);
    }
}