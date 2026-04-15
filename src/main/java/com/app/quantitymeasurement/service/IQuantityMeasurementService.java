package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.response.QuantityDTO;
import com.app.quantitymeasurement.dto.request.QuantityMeasurementDTO;

import java.util.List;

/**
 * Service interface for quantity operations like compare, convert,
 * arithmetic operations, and history retrieval.
 */
public interface IQuantityMeasurementService {

    /** Compare two quantities after converting to base unit */
    QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    /** Convert a quantity to the target unit */
    QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    /** Add two quantities (result in first unit) */
    QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    /** Add two quantities and convert result to target unit */
    QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO);

    /** Subtract second quantity from first (result in first unit) */
    QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    /** Subtract and convert result to target unit */
    QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO);

    /** Divide two quantities and return ratio */
    QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    /** Get history by operation type */
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    /** Get history by measurement type */
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    /** Count successful operations */
    long getOperationCount(String operation);

    /** Get all failed operations */
    List<QuantityMeasurementDTO> getErrorHistory();
}