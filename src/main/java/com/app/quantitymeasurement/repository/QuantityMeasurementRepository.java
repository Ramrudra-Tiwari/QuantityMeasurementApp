package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// JPA repository for QuantityMeasurementEntity
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    // find by operation
    List<QuantityMeasurementEntity> findByOperation(String operation);

    // find by measurement type
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    // find records after given time
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    // custom query: successful operations
    @Query("SELECT q FROM QuantityMeasurementEntity q " +
           "WHERE q.operation = :operation AND q.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(
            @Param("operation") String operation);

    // count successful operations
    long countByOperationAndErrorFalse(String operation);

    // find error records
    List<QuantityMeasurementEntity> findByErrorTrue();
}