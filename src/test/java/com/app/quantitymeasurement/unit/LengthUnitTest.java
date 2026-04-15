package com.app.quantitymeasurement.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for LengthUnit enum
class LengthUnitTest {

    private static final double EPSILON = 1e-6;

    // ===================== CONVERSION =====================

    @Test
    void testConversionFactors() {
        assertEquals(12.0, LengthUnit.FEET.convertToBaseUnit(1.0), EPSILON);
        assertEquals(1.0,  LengthUnit.INCHES.convertToBaseUnit(1.0), EPSILON);
        assertEquals(36.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPSILON);
        assertEquals(1.0 / 2.54,
            LengthUnit.CENTIMETERS.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit() {
        assertEquals(60.0, LengthUnit.FEET.convertToBaseUnit(5.0), EPSILON);
        assertEquals(12.0, LengthUnit.INCHES.convertToBaseUnit(12.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit() {
        assertEquals(1.0, LengthUnit.FEET.convertFromBaseUnit(12.0), EPSILON);
        assertEquals(30.48,
            LengthUnit.CENTIMETERS.convertFromBaseUnit(12.0), EPSILON);
    }

    // ===================== IDENTITY =====================

    @Test
    void testIdentity() {
        assertEquals("FEET", LengthUnit.FEET.getUnitName());
        assertEquals("LengthUnit", LengthUnit.FEET.getMeasurementType());
    }

    @Test
    void testEnumValues() {
        assertDoesNotThrow(() -> LengthUnit.valueOf("FEET"));
        assertTrue(LengthUnit.FEET instanceof Enum);
    }

    // ===================== ARITHMETIC =====================

    @Test
    void testSupportsArithmetic() {
        assertTrue(LengthUnit.FEET instanceof SupportsArithmetic);
        assertTrue(LengthUnit.INCHES instanceof SupportsArithmetic);
    }
}