package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for equals() and hashCode() of Quantity
class QuantityEqualityTest {

    private static final double EPSILON = 1e-6;

    // ===================== LENGTH =====================

    @Test
    void testLengthEquality() {
        assertEquals(new Quantity<>(1.0, LengthUnit.YARDS),
                     new Quantity<>(3.0, LengthUnit.FEET));

        assertEquals(new Quantity<>(1.0, LengthUnit.FEET),
                     new Quantity<>(12.0, LengthUnit.INCHES));

        assertNotEquals(new Quantity<>(1.0, LengthUnit.YARDS),
                        new Quantity<>(2.0, LengthUnit.FEET));
    }

    @Test
    void testLengthProperties() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(36.0, LengthUnit.INCHES);

        assertEquals(a, a); // reflexive
        assertTrue(a.equals(b) && b.equals(a)); // symmetric
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c)); // transitive
    }

    // ===================== WEIGHT =====================

    @Test
    void testWeightEquality() {
        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                     new Quantity<>(1000.0, WeightUnit.GRAM));

        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                     new Quantity<>(2.204624, WeightUnit.POUND));
    }

    // ===================== VOLUME =====================

    @Test
    void testVolumeEquality() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                     new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
    }

    // ===================== CROSS CATEGORY =====================

    @Test
    void testCrossCategoryNotEqual() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.FEET),
                        new Quantity<>(1.0, WeightUnit.KILOGRAM));
    }

    // ===================== PRECISION =====================

    @Test
    void testPrecisionTolerance() {
        assertEquals(new Quantity<>(1.00000008, LengthUnit.FEET),
                     new Quantity<>(1.0, LengthUnit.FEET));

        assertNotEquals(new Quantity<>(1.0000003, LengthUnit.FEET),
                        new Quantity<>(1.0, LengthUnit.FEET));
    }

    // ===================== VALIDATION =====================

    @Test
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(Double.NaN, LengthUnit.FEET));

        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));

        assertThrows(IllegalArgumentException.class,
            () -> new Quantity<>(1.0, (LengthUnit) null));
    }

    // ===================== HASHCODE =====================

    @Test
    void testHashCodeConsistency() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(36.0, LengthUnit.INCHES);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        Quantity<LengthUnit> a = new Quantity<>(1.0000003, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1.0, LengthUnit.FEET);

        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
}