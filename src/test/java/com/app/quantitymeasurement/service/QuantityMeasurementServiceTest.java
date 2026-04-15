package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.request.QuantityMeasurementDTO;
import com.app.quantitymeasurement.dto.response.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Tests for QuantityMeasurementServiceImpl
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuantityMeasurementServiceTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    private QuantityDTO feet;
    private QuantityDTO inches;
    private QuantityDTO kg;

    @BeforeEach
    void setUp() {
        feet   = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        inches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        kg     = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);

        when(repository.save(any(QuantityMeasurementEntity.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    }

    // ===================== COMPARE =====================

    @Test
    void testCompare() {
        assertEquals("true", service.compare(feet, inches).getResultString());
        assertEquals("false", service.compare(new QuantityDTO(2.0,
            QuantityDTO.LengthUnit.FEET), inches).getResultString());
    }

    @Test
    void testCompare_Invalid() {
        assertThrows(QuantityMeasurementException.class,
            () -> service.compare(feet, kg));

        verify(repository, atLeastOnce()).save(any());
    }

    // ===================== CONVERT =====================

    @Test
    void testConvert() {
        QuantityDTO target = new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES);

        assertEquals(12.0,
            service.convert(feet, target).getResultValue(), 1e-4);
    }

    // ===================== ADD =====================

    @Test
    void testAdd() {
        assertEquals(2.0,
            service.add(feet, inches).getResultValue(), 1e-4);

        QuantityDTO yards = new QuantityDTO(0.0, QuantityDTO.LengthUnit.YARDS);

        assertEquals("YARDS",
            service.add(feet, inches, yards).getResultUnit());
    }

    @Test
    void testAdd_Invalid() {
        assertThrows(QuantityMeasurementException.class,
            () -> service.add(feet, kg));
    }

    // ===================== SUBTRACT =====================

    @Test
    void testSubtract() {
        assertEquals(0.0,
            service.subtract(feet, inches).getResultValue(), 1e-4);
    }

    // ===================== DIVIDE =====================

    @Test
    void testDivide() {
        assertEquals(1.0,
            service.divide(feet, feet).getResultValue(), 1e-4);

        assertThrows(ArithmeticException.class,
            () -> service.divide(feet,
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES)));
    }

    // ===================== HISTORY =====================

    @Test
    void testHistory() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        e.setOperation("compare");

        when(repository.findByOperation("compare")).thenReturn(List.of(e));

        assertEquals(1,
            service.getHistoryByOperation("compare").size());
    }

    @Test
    void testCount() {
        when(repository.countByOperationAndErrorFalse("compare")).thenReturn(3L);

        assertEquals(3L, service.getOperationCount("compare"));
    }

    @Test
    void testErrorHistory() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        e.setError(true);

        when(repository.findByErrorTrue()).thenReturn(List.of(e));

        assertTrue(service.getErrorHistory().get(0).isError());
    }
}