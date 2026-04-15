package com.app.quantitymeasurement.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for IMeasurable implementations
class IMeasurableTest {

    private static final double EPSILON = 1e-6;

    // ===================== LENGTH =====================

    @Test
    void testLengthUnit() {
        IMeasurable feet = LengthUnit.FEET;

        assertEquals("FEET", feet.getUnitName());
        assertEquals("LengthUnit", feet.getMeasurementType());
        assertEquals(12.0, feet.convertToBaseUnit(1.0), EPSILON);
        assertEquals(1.0, feet.convertFromBaseUnit(12.0), EPSILON);
    }

    // ===================== WEIGHT =====================

    @Test
    void testWeightUnit() {
        IMeasurable kg = WeightUnit.KILOGRAM;

        assertEquals("KILOGRAM", kg.getUnitName());
        assertEquals("WeightUnit", kg.getMeasurementType());
        assertEquals(1.0, kg.convertToBaseUnit(1.0), EPSILON);
    }

    // ===================== VOLUME =====================

    @Test
    void testVolumeUnit() {
        IMeasurable litre = VolumeUnit.LITRE;

        assertEquals("LITRE", litre.getUnitName());
        assertEquals("VolumeUnit", litre.getMeasurementType());
        assertEquals(1.0, litre.convertToBaseUnit(1.0), EPSILON);
    }

    // ===================== TEMPERATURE =====================

    @Test
    void testTemperatureUnit() {
        IMeasurable c = TemperatureUnit.CELSIUS;

        assertEquals("CELSIUS", c.getUnitName());
        assertEquals("TemperatureUnit", c.getMeasurementType());
        assertEquals(100.0, c.convertToBaseUnit(100.0), EPSILON);
    }

    // ===================== ARITHMETIC SUPPORT =====================

    @Test
    void testSupportsArithmetic() {
        assertTrue(LengthUnit.FEET instanceof SupportsArithmetic);
        assertTrue(WeightUnit.KILOGRAM instanceof SupportsArithmetic);
        assertTrue(VolumeUnit.LITRE instanceof SupportsArithmetic);

        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
        assertFalse(TemperatureUnit.FAHRENHEIT.supportsArithmetic());
        assertFalse(TemperatureUnit.KELVIN.supportsArithmetic());
    }
}