package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for QuantityModel POJO
class QuantityModelTest {

    private static final double EPSILON = 1e-6;

    // ===================== CONSTRUCTOR =====================

    @Test
    void testConstructor_ValidValues() {
        assertEquals(5.0,
            new QuantityModel<>(5.0, LengthUnit.FEET).getValue(), EPSILON);

        assertEquals(WeightUnit.KILOGRAM,
            new QuantityModel<>(10.0, WeightUnit.KILOGRAM).getUnit());

        assertEquals(VolumeUnit.LITRE,
            new QuantityModel<>(3.5, VolumeUnit.LITRE).getUnit());

        assertEquals(TemperatureUnit.CELSIUS,
            new QuantityModel<>(25.0, TemperatureUnit.CELSIUS).getUnit());
    }

    @Test
    void testConstructor_ZeroAndNegativeValues() {
        assertEquals(0.0,
            new QuantityModel<>(0.0, LengthUnit.INCHES).getValue(), EPSILON);

        assertEquals(-5.0,
            new QuantityModel<>(-5.0, WeightUnit.GRAM).getValue(), EPSILON);
    }

    @Test
    void testConstructor_InvalidInputs() {
        assertThrows(IllegalArgumentException.class,
            () -> new QuantityModel<>(10.0, null));

        assertThrows(IllegalArgumentException.class,
            () -> new QuantityModel<>(Double.NaN, LengthUnit.FEET));

        assertThrows(IllegalArgumentException.class,
            () -> new QuantityModel<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    // ===================== GETTERS =====================

    @Test
    void testGetters() {
        QuantityModel<LengthUnit> model = new QuantityModel<>(3.0, LengthUnit.YARDS);

        assertEquals(3.0, model.getValue(), EPSILON);
        assertEquals("YARDS", model.getUnit().getUnitName());
        assertEquals("LengthUnit", model.getUnit().getMeasurementType());
    }

    // ===================== toString =====================

    @Test
    void testToString() {
        QuantityModel<VolumeUnit> model = new QuantityModel<>(3.5, VolumeUnit.LITRE);
        String s = model.toString();

        assertTrue(s.contains("3.5"));
        assertTrue(s.contains("LITRE"));
    }
}