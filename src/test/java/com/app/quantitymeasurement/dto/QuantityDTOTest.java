package com.app.quantitymeasurement.dto;
// Tests for QuantityDTO behavior and enums
import com.app.quantitymeasurement.dto.response.QuantityDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class QuantityDTOTest {

    // ===================== CONSTRUCTOR (ENUM) =====================

    @Test
    void testConstructor_LengthUnit_Feet() {
        QuantityDTO dto = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        assertEquals(2.0, dto.getValue());
        assertEquals("FEET", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_LengthUnit_Inches() {
        QuantityDTO dto = new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES);
        assertEquals("INCHES", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_LengthUnit_Yards() {
        QuantityDTO dto = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARDS);
        assertEquals("YARDS", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_LengthUnit_Centimeters() {
        QuantityDTO dto = new QuantityDTO(30.48, QuantityDTO.LengthUnit.CENTIMETERS);
        assertEquals("CENTIMETERS", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_VolumeUnit_Litre() {
        QuantityDTO dto = new QuantityDTO(3.0, QuantityDTO.VolumeUnit.LITRE);
        assertEquals("LITRE", dto.getUnit());
        assertEquals("VolumeUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_WeightUnit_Kilogram() {
        QuantityDTO dto = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        assertEquals("KILOGRAM", dto.getUnit());
        assertEquals("WeightUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_TemperatureUnit_Celsius() {
        QuantityDTO dto = new QuantityDTO(25.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertEquals("CELSIUS", dto.getUnit());
        assertEquals("TemperatureUnit", dto.getMeasurementType());
    }

    // ===================== STRING CONSTRUCTOR =====================

    @Test
    void testConstructor_StringBased() {
        QuantityDTO dto = new QuantityDTO(10.0, "FEET", "LengthUnit");
        assertEquals(10.0, dto.getValue());
        assertEquals("FEET", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    // ===================== GETTERS =====================

    @Test
    void testGetValue() {
        assertEquals(42.5, new QuantityDTO(42.5, QuantityDTO.LengthUnit.FEET).getValue());
    }

    @Test
    void testGetUnit() {
        assertEquals("YARDS", new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARDS).getUnit());
    }

    @Test
    void testGetMeasurementType() {
        assertEquals("WeightUnit",
            new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM).getMeasurementType());
    }

    // ===================== toString =====================

    @Test
    void testToString_ContainsValueAndUnit() {
        QuantityDTO dto = new QuantityDTO(2.5, QuantityDTO.LengthUnit.FEET);
        String s = dto.toString();
        assertTrue(s.contains("2.5"));
        assertTrue(s.contains("FEET"));
    }

    // ===================== ENUM VALIDATION =====================

    @Test
    void testLengthUnit_ValuesExist() {
        assertDoesNotThrow(() -> QuantityDTO.LengthUnit.valueOf("FEET"));
        assertDoesNotThrow(() -> QuantityDTO.LengthUnit.valueOf("INCHES"));
    }

    @Test
    void testVolumeUnit_ValuesExist() {
        assertDoesNotThrow(() -> QuantityDTO.VolumeUnit.valueOf("LITRE"));
    }

    @Test
    void testWeightUnit_ValuesExist() {
        assertDoesNotThrow(() -> QuantityDTO.WeightUnit.valueOf("KILOGRAM"));
    }

    @Test
    void testTemperatureUnit_ValuesExist() {
        assertDoesNotThrow(() -> QuantityDTO.TemperatureUnit.valueOf("CELSIUS"));
    }

    // ===================== INTERFACE =====================

    @Test
    void testIMeasurableUnit_Implemented() {
        assertTrue(QuantityDTO.LengthUnit.FEET instanceof QuantityDTO.IMeasurableUnit);
    }

    // ===================== VALIDATION =====================

    @Test
    void testValidation_ValidUnit() {
        QuantityDTO dto = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertTrue(dto.isUnitValidForMeasurementType());
    }

    @Test
    void testValidation_InvalidUnit() {
        QuantityDTO dto = new QuantityDTO(1.0, "INVALID_UNIT", "LengthUnit");
        assertFalse(dto.isUnitValidForMeasurementType());
    }

    @Test
    void testValidation_NullFields() {
        QuantityDTO dto = new QuantityDTO();
        assertTrue(dto.isUnitValidForMeasurementType());
    }
}