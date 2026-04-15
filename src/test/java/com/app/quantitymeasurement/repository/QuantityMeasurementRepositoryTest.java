package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Repository tests using @DataJpaTest
@DataJpaTest
@ActiveProfiles("test")
class QuantityMeasurementRepositoryTest {

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // ===================== BASIC =====================

    @Test
    void testSaveAndFindAll() {
        repository.save(buildEntity("compare", false));

        List<QuantityMeasurementEntity> all = repository.findAll();

        assertEquals(1, all.size());
        assertEquals("compare", all.get(0).getOperation());
    }

    @Test
    void testSave_GeneratesId() {
        QuantityMeasurementEntity saved = repository.save(buildEntity("add", false));
        assertNotNull(saved.getId());
    }

    // ===================== FIND =====================

    @Test
    void testFindByOperation() {
        repository.save(buildEntity("compare", false));
        repository.save(buildEntity("add", false));

        List<QuantityMeasurementEntity> result = repository.findByOperation("compare");

        assertEquals(1, result.size());
    }

    @Test
    void testFindByMeasurementType() {
        repository.save(buildEntity("compare", false));

        List<QuantityMeasurementEntity> result =
            repository.findByThisMeasurementType("LengthUnit");

        assertEquals(1, result.size());
    }

    @Test
    void testFindByCreatedAtAfter() {
        repository.save(buildEntity("compare", false));

        List<QuantityMeasurementEntity> result =
            repository.findByCreatedAtAfter(LocalDateTime.now().minusSeconds(1));

        assertFalse(result.isEmpty());
    }

    // ===================== CUSTOM =====================

    @Test
    void testFindSuccessfulByOperation() {
        repository.save(buildEntity("add", false));
        repository.save(buildEntity("add", true));

        List<QuantityMeasurementEntity> result =
            repository.findSuccessfulByOperation("add");

        assertEquals(1, result.size());
        assertFalse(result.get(0).isError());
    }

    @Test
    void testCountByOperation() {
        repository.save(buildEntity("compare", false));
        repository.save(buildEntity("compare", false));
        repository.save(buildEntity("compare", true));

        long count = repository.countByOperationAndErrorFalse("compare");

        assertEquals(2, count);
    }

    @Test
    void testFindErrors() {
        repository.save(buildEntity("add", false));
        repository.save(buildEntity("add", true));

        List<QuantityMeasurementEntity> errors = repository.findByErrorTrue();

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).isError());
    }

    // ===================== HELPER =====================

    private QuantityMeasurementEntity buildEntity(String operation, boolean isError) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();

        e.setThisValue(1.0);
        e.setThisUnit("FEET");
        e.setThisMeasurementType("LengthUnit");

        e.setThatValue(12.0);
        e.setThatUnit("INCHES");
        e.setThatMeasurementType("LengthUnit");

        e.setOperation(operation);
        e.setResultString("true");
        e.setResultValue(1.0);

        e.setError(isError);
        e.setErrorMessage(isError ? "Error" : null);

        return e;
    }
}