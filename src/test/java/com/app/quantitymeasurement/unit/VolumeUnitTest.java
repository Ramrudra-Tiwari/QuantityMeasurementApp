package com.app.quantitymeasurement.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for VolumeUnit enum
class VolumeUnitTest {

    private static final double EPSILON = 1e-6;

    // ===================== CONVERSION =====================

    @Test
    void testConversionFactors() {
        assertEquals(1.0, VolumeUnit.LITRE.convertToBaseUnit(1.0), EPSILON);
        assertEquals(0.001, VolumeUnit.MILLILITRE.convertToBaseUnit(1.0), EPSILON);
        assertEquals(3.785412, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPSILON);
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON);
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.785412), EPSILON);
    }

    // ===================== IDENTITY =====================

    @Test
    void testIdentity() {
        assertEquals("LITRE", VolumeUnit.LITRE.getUnitName());
        assertEquals("VolumeUnit", VolumeUnit.LITRE.getMeasurementType());
    }

    @Test
    void testEnumValues() {
        assertDoesNotThrow(() -> VolumeUnit.valueOf("LITRE"));
        assertTrue(VolumeUnit.LITRE instanceof Enum);
    }

    // ===================== ARITHMETIC =====================

    @Test
    void testSupportsArithmetic() {
        assertTrue(VolumeUnit.LITRE instanceof SupportsArithmetic);
        assertTrue(VolumeUnit.MILLILITRE instanceof SupportsArithmetic);
        assertTrue(VolumeUnit.GALLON instanceof SupportsArithmetic);
    }
}