package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Quantity.convertTo()
class QuantityConversionTest {

    private static final double EPSILON = 1e-6;

    // ===================== LENGTH =====================

    @Test
    void testLengthConversion() {
        assertEquals(12.0,
            new Quantity<>(1.0, LengthUnit.FEET)
                .convertTo(LengthUnit.INCHES).getValue(), EPSILON);

        assertEquals(2.0,
            new Quantity<>(24.0, LengthUnit.INCHES)
                .convertTo(LengthUnit.FEET).getValue(), EPSILON);
    }

    @Test
    void testLengthEdgeCases() {
        assertEquals(new Quantity<>(5.0, LengthUnit.FEET),
            new Quantity<>(5.0, LengthUnit.FEET).convertTo(LengthUnit.FEET));

        assertEquals(new Quantity<>(0.0, LengthUnit.INCHES),
            new Quantity<>(0.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES));

        assertEquals(new Quantity<>(-12.0, LengthUnit.INCHES),
            new Quantity<>(-1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES));
    }

    @Test
    void testLengthRoundTrip() {
        Quantity<LengthUnit> original = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(original,
            original.convertTo(LengthUnit.INCHES).convertTo(LengthUnit.FEET));
    }

    // ===================== WEIGHT =====================

    @Test
    void testWeightConversion() {
        assertEquals(2.204624,
            new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.POUND).getValue(), EPSILON);

        assertEquals(1000.0,
            new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM).getValue(), EPSILON);
    }

    // ===================== VOLUME =====================

    @Test
    void testVolumeConversion() {
        assertEquals(1000.0,
            new Quantity<>(1.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE).getValue(), EPSILON);

        assertEquals(1.0,
            new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE).getValue(), EPSILON);
    }

    @Test
    void testVolumeRoundTrip() {
        Quantity<VolumeUnit> original = new Quantity<>(1.5, VolumeUnit.LITRE);
        assertEquals(original,
            original.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE));
    }

    // ===================== TEMPERATURE =====================

    @Test
    void testTemperatureConversion() {
        assertEquals(32.0,
            new Quantity<>(0.0, TemperatureUnit.CELSIUS)
                .convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPSILON);

        assertEquals(212.0,
            new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                .convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPSILON);
    }

    @Test
    void testTemperatureRoundTrip() {
        double original = 37.5;

        Quantity<TemperatureUnit> q =
            new Quantity<>(original, TemperatureUnit.CELSIUS);

        assertEquals(original,
            q.convertTo(TemperatureUnit.FAHRENHEIT)
             .convertTo(TemperatureUnit.CELSIUS)
             .getValue(), 1e-4);
    }

    // ===================== VALIDATION =====================

    @Test
    void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(1.0, LengthUnit.FEET).convertTo(null));

        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(Double.NaN, LengthUnit.FEET));

        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }
}