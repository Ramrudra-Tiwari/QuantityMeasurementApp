package com.app.quantitymeasurement.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for WeightUnit enum
class WeightUnitTest {

    private static final double EPSILON = 1e-6;

    // ===================== CONVERSION =====================

    @Test
    void testConversionFactors() {
        assertEquals(1.0, WeightUnit.KILOGRAM.convertToBaseUnit(1.0), EPSILON);
        assertEquals(0.001, WeightUnit.GRAM.convertToBaseUnit(1.0), EPSILON);
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit() {
        assertEquals(1.0, WeightUnit.GRAM.convertToBaseUnit(1000.0), EPSILON);
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit() {
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), EPSILON);
        assertEquals(2.204624, WeightUnit.POUND.convertFromBaseUnit(1.0), EPSILON);
    }

    // ===================== IDENTITY =====================

    @Test
    void testIdentity() {
        assertEquals("KILOGRAM", WeightUnit.KILOGRAM.getUnitName());
        assertEquals("WeightUnit", WeightUnit.KILOGRAM.getMeasurementType());
    }

    @Test
    void testEnumValues() {
        assertDoesNotThrow(() -> WeightUnit.valueOf("KILOGRAM"));
        assertTrue(WeightUnit.KILOGRAM instanceof Enum);
    }

    // ===================== ARITHMETIC =====================

    @Test
    void testSupportsArithmetic() {
        assertTrue(WeightUnit.KILOGRAM instanceof SupportsArithmetic);
        assertTrue(WeightUnit.GRAM instanceof SupportsArithmetic);
        assertTrue(WeightUnit.POUND instanceof SupportsArithmetic);
    }
}