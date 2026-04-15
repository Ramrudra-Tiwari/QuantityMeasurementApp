package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Quantity arithmetic operations
class QuantityArithmeticTest {

    private static final double EPSILON = 1e-6;

    // ===================== ADD =====================

    @Test
    void testAdd_Basic() {
        assertEquals(new Quantity<>(3.0, LengthUnit.FEET),
            new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(2.0, LengthUnit.FEET)));

        assertEquals(new Quantity<>(2.0, LengthUnit.FEET),
            new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testAdd_ExplicitTarget() {
        assertEquals(new Quantity<>(24.0, LengthUnit.INCHES),
            new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.INCHES));
    }

    @Test
    void testAdd_OtherUnits() {
        assertEquals(new Quantity<>(3.0, WeightUnit.KILOGRAM),
            new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(2.0, WeightUnit.KILOGRAM)));

        assertEquals(new Quantity<>(2.0, VolumeUnit.LITRE),
            new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    // ===================== SUBTRACT =====================

    @Test
    void testSubtract_Basic() {
        assertEquals(new Quantity<>(5.0, LengthUnit.FEET),
            new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET)));

        assertEquals(new Quantity<>(9.5, LengthUnit.FEET),
            new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCHES)));
    }

    // ===================== DIVIDE =====================

    @Test
    void testDivide_Basic() {
        assertEquals(5.0,
            new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);

        assertEquals(1.0,
            new Quantity<>(24.0, LengthUnit.INCHES)
                .divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivide_ByZero() {
        assertThrows(ArithmeticException.class,
            () -> new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(0.0, LengthUnit.FEET)));
    }

    // ===================== IMMUTABILITY =====================

    @Test
    void testImmutability() {
        Quantity<VolumeUnit> original = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> result = original.add(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

        assertEquals(new Quantity<>(5.0, VolumeUnit.LITRE), original);
        assertNotSame(original, result);
    }

    // ===================== VALIDATION =====================

    @Test
    void testNullOperands() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q.add(null));
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    @Test
    void testCrossCategory() {
        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> length.add((Quantity) weight));
    }

    // ===================== TEMPERATURE =====================

    @Test
    void testTemperature_NotSupported() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class, () -> a.add(b));
        assertThrows(UnsupportedOperationException.class, () -> a.subtract(b));
        assertThrows(UnsupportedOperationException.class, () -> a.divide(b));
    }

    // ===================== COLLECTION =====================

    @Test
    void testHashSet_EqualQuantities() {
        Set<Quantity<VolumeUnit>> set = new HashSet<>();
        set.add(new Quantity<>(1.0, VolumeUnit.LITRE));
        set.add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

        assertEquals(1, set.size());
    }

    // ===================== INTERNAL =====================

    @Test
    void testArithmeticEnumExists() {
        Class<?> enumClass = findInnerEnum(Quantity.class, "ArithmeticOperation");
        assertNotNull(enumClass);
    }

    @Test
    void testPrivateHelperExists() throws Exception {
        Method method = Quantity.class.getDeclaredMethod(
            "performArithmetic", Quantity.class,
            findInnerEnum(Quantity.class, "ArithmeticOperation"));

        assertTrue(Modifier.isPrivate(method.getModifiers()));
    }

    // ===================== HELPER =====================

    private static Class<?> findInnerEnum(Class<?> outer, String enumName) {
        for (Class<?> c : outer.getDeclaredClasses()) {
            if (c.isEnum() && c.getSimpleName().equals(enumName)) return c;
        }
        return null;
    }
}