package com.app.quantitymeasurementapp.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;

@Repository
public interface QuantityMeasurentRepository extends JpaRepository<QuantityMeasurementEntity,Long> {
	List<QuantityMeasurementEntity> findByOperation(String operation);

	List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

	
	List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

	
	@Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = : operation "+
	"AND e. isError = false")
	List<QuantityMeasurementEntity> findSuccessfulOperations(
	@Param("operation") String operation

	);

	long countByOperationAndIsErrorFalse(String operation);

	List<QuantityMeasurementEntity> findByIsErrorTrue();
}
