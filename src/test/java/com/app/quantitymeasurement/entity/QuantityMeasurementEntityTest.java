package com.app.quantitymeasurement.entity;
// Tests for QuantityMeasurementEntity behavior
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;class QuantityMeasurementEntityTest {

    private QuantityModel<IMeasurable> q1;
    private QuantityModel<IMeasurable> q2;
    private QuantityModel<IMeasurable> result;

    @BeforeEach
    void setUp() {
        q1 = new QuantityModel<>(2.0, LengthUnit.FEET);
        q2 = new QuantityModel<>(24.0, LengthUnit.INCHES);
        result = new QuantityModel<>(4.0, LengthUnit.FEET);
    }

    // ===================== STRING RESULT =====================

    @Test
    void testStringResultConstructor_StoresData() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Equal");

        assertEquals(2.0, entity.getThisValue(), 1e-6);
        assertEquals("FEET", entity.getThisUnit());
        assertEquals(24.0, entity.getThatValue(), 1e-6);
        assertEquals("INCHES", entity.getThatUnit());
        assertEquals("COMPARE", entity.getOperation());
        assertEquals("Equal", entity.getResultString());
        assertFalse(entity.isError());
    }

    // ===================== MODEL RESULT =====================

    @Test
    void testModelResultConstructor_StoresData() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "ADD", result);

        assertEquals(4.0, entity.getResultValue(), 1e-6);
        assertEquals("FEET", entity.getResultUnit());
        assertEquals("LengthUnit", entity.getResultMeasurementType());
        assertNull(entity.getResultString());
        assertFalse(entity.isError());
    }

    // ===================== ERROR =====================

    @Test
    void testErrorConstructor() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "DIVIDE", "Division by zero", true);

        assertTrue(entity.isError());
        assertEquals("Division by zero", entity.getErrorMessage());
        assertNull(entity.getResultValue());
    }

    // ===================== NULL GUARD =====================

    @Test
    void testNullOperands_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new QuantityMeasurementEntity(null, q2, "COMPARE", "Equal"));

        assertThrows(IllegalArgumentException.class,
            () -> new QuantityMeasurementEntity(q1, null, "COMPARE", "Equal"));
    }

    // ===================== EQUALS =====================

    @Test
    void testEquals_SameOperands() {
        QuantityMeasurementEntity e1 =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Equal");
        QuantityMeasurementEntity e2 =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Not Equal");

        assertEquals(e1, e2);
    }

    @Test
    void testEquals_DifferentOperation() {
        QuantityMeasurementEntity e1 =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Equal");
        QuantityMeasurementEntity e2 =
            new QuantityMeasurementEntity(q1, q2, "ADD", result);

        assertNotEquals(e1, e2);
    }

    @Test
    void testEquals_NullAndDifferentType() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Equal");

        assertNotEquals(entity, null);
        assertFalse(entity.equals("string"));
    }

    // ===================== toString =====================

    @Test
    void testToString_Success() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "ADD", result);

        String s = entity.toString();
        assertTrue(s.contains("[SUCCESS]"));
        assertTrue(s.contains("ADD"));
        assertTrue(s.contains("FEET"));
    }

    @Test
    void testToString_Error() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "DIVIDE", "error", true);

        String s = entity.toString();
        assertTrue(s.contains("[ERROR]"));
    }

    // ===================== SERIALIZABLE =====================

    @Test
    void testSerializable() {
        QuantityMeasurementEntity entity =
            new QuantityMeasurementEntity(q1, q2, "COMPARE", "Equal");

        assertTrue(entity instanceof java.io.Serializable);
    }
}