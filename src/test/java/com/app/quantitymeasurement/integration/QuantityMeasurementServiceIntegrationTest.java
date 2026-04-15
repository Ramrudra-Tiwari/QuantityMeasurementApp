package com.app.quantitymeasurement.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.app.quantitymeasurement.dto.request.QuantityMeasurementDTO;
import com.app.quantitymeasurement.dto.response.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;

// Service-level tests using Mockito
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuantityMeasurementServiceIntegrationTest {

    private static final double EPSILON = 1e-6;

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        when(repository.save(any(QuantityMeasurementEntity.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    }

    // ===================== COMPARE =====================

    @Test
    void testCompare_Length() {
        assertTrue(Bool(service.compare(feet(2), inches(24))));
        assertFalse(Bool(service.compare(feet(1), inches(24))));
    }

    @Test
    void testCompare_OtherUnits() {
        assertTrue(Bool(service.compare(yards(1), feet(3))));
        assertTrue(Bool(service.compare(kg(1), gram(1000))));
        assertTrue(Bool(service.compare(litre(1), ml(1000))));
    }

    // ===================== CONVERT =====================

    @Test
    void testConvert() {
        assertEquals("YARDS", service.convert(inches(24), yards(0)).getResultUnit());
        assertEquals(24.0, service.convert(feet(2), inches(0)).getResultValue(), EPSILON);
    }

    // ===================== ADD =====================

    @Test
    void testAdd() {
        assertEquals(4.0, service.add(feet(2), inches(24)).getResultValue(), EPSILON);
        assertEquals("YARDS", service.add(feet(2), inches(24), yards(0)).getResultUnit());
    }

    // ===================== SUBTRACT =====================

    @Test
    void testSubtract() {
        assertEquals(0.0, service.subtract(feet(2), inches(24)).getResultValue(), EPSILON);
    }

    // ===================== DIVIDE =====================

    @Test
    void testDivide() {
        assertEquals(1.0, service.divide(feet(2), inches(24)).getResultValue(), EPSILON);
    }

    // ===================== EXCEPTIONS =====================

    @Test
    void testExceptions() {
        assertThrows(ArithmeticException.class,
            () -> service.divide(feet(1), inches(0)));

        assertThrows(QuantityMeasurementException.class,
            () -> service.add(feet(1), kg(1)));
    }

    // ===================== REPOSITORY =====================

    @Test
    void testRepositorySaveCalled() {
        service.add(feet(1), inches(12));
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // ===================== FLOW =====================

    @Test
    void testFullFlow() {
        assertTrue(Bool(service.compare(feet(2), inches(24))));
        assertEquals(4.0, service.add(feet(2), inches(24)).getResultValue(), EPSILON);
        assertEquals(1.0, service.divide(feet(2), inches(24)).getResultValue(), EPSILON);
    }

    // ===================== HELPERS =====================

    private boolean Bool(QuantityMeasurementDTO dto) {
        return "true".equals(dto.getResultString());
    }

    private QuantityDTO feet(double v) {
        return new QuantityDTO(v, QuantityDTO.LengthUnit.FEET);
    }

    private QuantityDTO inches(double v) {
        return new QuantityDTO(v, QuantityDTO.LengthUnit.INCHES);
    }

    private QuantityDTO yards(double v) {
        return new QuantityDTO(v, QuantityDTO.LengthUnit.YARDS);
    }

    private QuantityDTO kg(double v) {
        return new QuantityDTO(v, QuantityDTO.WeightUnit.KILOGRAM);
    }

    private QuantityDTO gram(double v) {
        return new QuantityDTO(v, QuantityDTO.WeightUnit.GRAM);
    }

    private QuantityDTO litre(double v) {
        return new QuantityDTO(v, QuantityDTO.VolumeUnit.LITRE);
    }

    private QuantityDTO ml(double v) {
        return new QuantityDTO(v, QuantityDTO.VolumeUnit.MILLILITRE);
    }
}