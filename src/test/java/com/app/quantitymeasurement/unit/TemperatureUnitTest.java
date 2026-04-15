package com.app.quantitymeasurement.unit;

import com.app.quantitymeasurement.model.Quantity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for TemperatureUnit enum
class TemperatureUnitTest {

    private static final double EPSILON = 1e-6;

    // ===================== IDENTITY =====================

    @Test
    void testIdentity() {
        assertEquals("CELSIUS", TemperatureUnit.CELSIUS.getUnitName());
        assertEquals("TemperatureUnit", TemperatureUnit.CELSIUS.getMeasurementType());
    }

    // ===================== CONVERSION =====================

    @Test
    void testConvertToBaseUnit() {
        assertEquals(0.0,
            TemperatureUnit.FAHRENHEIT.convertToBaseUnit(32.0), EPSILON);

        assertEquals(100.0,
            TemperatureUnit.FAHRENHEIT.convertToBaseUnit(212.0), EPSILON);

        assertEquals(-273.15,
            TemperatureUnit.KELVIN.convertToBaseUnit(0.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit() {
        assertEquals(32.0,
            TemperatureUnit.FAHRENHEIT.convertFromBaseUnit(0.0), EPSILON);

        assertEquals(273.15,
            TemperatureUnit.KELVIN.convertFromBaseUnit(0.0), EPSILON);
    }

    // ===================== ARITHMETIC =====================

    @Test
    void testNoArithmeticSupport() {
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
        assertFalse(TemperatureUnit.FAHRENHEIT.supportsArithmetic());
        assertFalse(TemperatureUnit.KELVIN.supportsArithmetic());
    }

    @Test
    void testArithmeticRejected() {
        assertThrows(UnsupportedOperationException.class,
            () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                .add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));

        assertThrows(UnsupportedOperationException.class,
            () -> new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT)
                .divide(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
    }

    // ===================== INTERFACE =====================

    @Test
    void testImplementsIMeasurable() {
        assertTrue(IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
    }
}