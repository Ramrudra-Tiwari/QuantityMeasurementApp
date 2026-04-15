package com.app.quantitymeasurement.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.DoubleBinaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.dto.response.QuantityDTO;
import com.app.quantitymeasurement.dto.response.QuantityDTO.LengthUnit;
import com.app.quantitymeasurement.dto.request.QuantityMeasurementDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * Service implementation for quantity operations.
 */
@Slf4j
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    /** Supported operations */
    private enum Operation { COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE }

    /** Arithmetic types */
    private enum ArithmeticOperation { ADD, SUBTRACT, DIVIDE }

    /** Compare two quantities */
    @Override
    public QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        QuantityModel<IMeasurable> q1 = convertDtoToModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = convertDtoToModel(thatQuantityDTO);

        try {
            if (!q1.getUnit().getMeasurementType().equals(q2.getUnit().getMeasurementType())) {
                throw new QuantityMeasurementException(
                    "compare Error: Different measurement types");
            }

            boolean result = compareBaseValues(q1, q2);

            QuantityMeasurementEntity entity = buildEntity(
                q1, q2, Operation.COMPARE.name().toLowerCase(),
                String.valueOf(result), null, null, null, false, null);

            repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            saveErrorEntity(q1, q2, Operation.COMPARE.name().toLowerCase(), e.getMessage());
            throw new QuantityMeasurementException("compare Error: " + e.getMessage(), e);
        }
    }

    /** Convert quantity */
    @Override
    public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO targetUnitDTO) {
        QuantityModel<IMeasurable> source = convertDtoToModel(thisQuantityDTO);
        QuantityModel<IMeasurable> target = convertDtoToModel(targetUnitDTO);

        try {
            double result = (source.getUnit() instanceof com.app.quantitymeasurement.unit.TemperatureUnit)
                ? convertTemperatureUnit(source, target.getUnit())
                : target.getUnit().convertFromBaseUnit(
                    source.getUnit().convertToBaseUnit(source.getValue()));

            QuantityMeasurementEntity entity = buildEntity(
                source, target, Operation.CONVERT.name().toLowerCase(),
                null, result,
                target.getUnit().getUnitName(),
                target.getUnit().getMeasurementType(),
                false, null);

            repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            saveErrorEntity(source, target, Operation.CONVERT.name().toLowerCase(), e.getMessage());
            throw new QuantityMeasurementException("convert Error: " + e.getMessage(), e);
        }
    }

    /** Add (default unit) */
    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    /** Add with target unit */
    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO,
                                      QuantityDTO thatQuantityDTO,
                                      QuantityDTO targetUnitDTO) {
        QuantityModel<IMeasurable> q1 = convertDtoToModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = convertDtoToModel(thatQuantityDTO);
        QuantityModel<IMeasurable> target = convertDtoToModel(targetUnitDTO);

        try {
            validateArithmeticOperands(q1, q2, target.getUnit(), true);

            double result = target.getUnit().convertFromBaseUnit(
                performArithmetic(q1, q2, ArithmeticOperation.ADD));

            QuantityMeasurementEntity entity = buildEntity(
                q1, q2, Operation.ADD.name().toLowerCase(),
                null, result,
                target.getUnit().getUnitName(),
                target.getUnit().getMeasurementType(),
                false, null);

            repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            saveErrorEntity(q1, q2, Operation.ADD.name().toLowerCase(), e.getMessage());
            throw new QuantityMeasurementException("add Error: " + e.getMessage(), e);
        }
    }

    /** Subtract (default unit) */
    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    /** Subtract with target unit */
    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO,
                                           QuantityDTO thatQuantityDTO,
                                           QuantityDTO targetUnitDTO) {
        QuantityModel<IMeasurable> q1 = convertDtoToModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = convertDtoToModel(thatQuantityDTO);
        QuantityModel<IMeasurable> target = convertDtoToModel(targetUnitDTO);

        try {
            validateArithmeticOperands(q1, q2, target.getUnit(), true);

            double result = target.getUnit().convertFromBaseUnit(
                performArithmetic(q1, q2, ArithmeticOperation.SUBTRACT));

            QuantityMeasurementEntity entity = buildEntity(
                q1, q2, Operation.SUBTRACT.name().toLowerCase(),
                null, result,
                target.getUnit().getUnitName(),
                target.getUnit().getMeasurementType(),
                false, null);

            repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            saveErrorEntity(q1, q2, Operation.SUBTRACT.name().toLowerCase(), e.getMessage());
            throw new QuantityMeasurementException("subtract Error: " + e.getMessage(), e);
        }
    }

    /** Divide */
    @Override
    public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        QuantityModel<IMeasurable> q1 = convertDtoToModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = convertDtoToModel(thatQuantityDTO);

        try {
            validateArithmeticOperands(q1, q2, null, false);

            double result = performArithmetic(q1, q2, ArithmeticOperation.DIVIDE);

            QuantityMeasurementEntity entity = buildEntity(
                q1, q2, Operation.DIVIDE.name().toLowerCase(),
                null, result, null, null, false, null);

            repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            saveErrorEntity(q1, q2, Operation.DIVIDE.name().toLowerCase(), e.getMessage());
            throw new QuantityMeasurementException("divide Error: " + e.getMessage(), e);
        }
    }

    /** History by operation */
    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation));
    }

    /** History by type */
    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
            repository.findByThisMeasurementType(measurementType));
    }

    /** Count operations */
    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndErrorFalse(operation);
    }

    /** Error history */
    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }

    /** DTO → Model */
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO quantity) {
        if (quantity == null) throw new IllegalArgumentException("QuantityDTO cannot be null");

        return new QuantityModel<>(quantity.getValue(),
            getModelUnit(quantity.getMeasurementType(), quantity.getUnit()));
    }

    /** Get unit */
    private IMeasurable getModelUnit(String measurementType, String unit) {
        switch (measurementType) {
            case "LengthUnit": return com.app.quantitymeasurement.unit.LengthUnit.valueOf(unit);
            case "WeightUnit": return com.app.quantitymeasurement.unit.WeightUnit.valueOf(unit);
            case "VolumeUnit": return com.app.quantitymeasurement.unit.VolumeUnit.valueOf(unit);
            case "TemperatureUnit": return com.app.quantitymeasurement.unit.TemperatureUnit.valueOf(unit);
            default: throw new IllegalArgumentException("Unsupported type");
        }
    }

    /** Compare base values */
    private <U extends IMeasurable> boolean compareBaseValues(
            QuantityModel<U> q1, QuantityModel<U> q2) {
        return Double.compare(
            q1.getUnit().convertToBaseUnit(q1.getValue()),
            q2.getUnit().convertToBaseUnit(q2.getValue())) == 0;
    }

    /** Convert temperature */
    private <U extends IMeasurable> double convertTemperatureUnit(
            QuantityModel<U> source, U targetUnit) {
        return targetUnit.convertFromBaseUnit(
            source.getUnit().convertToBaseUnit(source.getValue()));
    }

    /** Validate arithmetic */
    private <U extends IMeasurable> void validateArithmeticOperands(
            QuantityModel<U> q1, QuantityModel<U> q2, U targetUnit, boolean targetRequired) {

        if (q1 == null || q2 == null)
            throw new IllegalArgumentException("Operands cannot be null");

        if (!q1.getUnit().getMeasurementType()
                .equals(q2.getUnit().getMeasurementType()))
            throw new IllegalArgumentException("Different measurement types");

        if (q1.getUnit().getMeasurementType().equals("TemperatureUnit"))
            throw new UnsupportedOperationException("Temperature not supported");

        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target required");
    }

    /** Perform arithmetic */
    private <U extends IMeasurable> double performArithmetic(
            QuantityModel<U> q1, QuantityModel<U> q2, ArithmeticOperation operation) {

        double base1 = q1.getUnit().convertToBaseUnit(q1.getValue());
        double base2 = q2.getUnit().convertToBaseUnit(q2.getValue());

        if (operation == ArithmeticOperation.DIVIDE && base2 == 0)
            throw new ArithmeticException("Divide by zero");

        DoubleBinaryOperator op = switch (operation) {
            case ADD -> (a, b) -> a + b;
            case SUBTRACT -> (a, b) -> a - b;
            case DIVIDE -> (a, b) -> a / b;
        };

        return op.applyAsDouble(base1, base2);
    }

    /** Build entity */
    private QuantityMeasurementEntity buildEntity(
            QuantityModel<IMeasurable> q1,
            QuantityModel<IMeasurable> q2,
            String operation,
            String resultString,
            Double resultValue,
            String resultUnit,
            String resultType,
            boolean isError,
            String errorMessage) {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit().getUnitName());
        entity.setThisMeasurementType(q1.getUnit().getMeasurementType());

        entity.setThatValue(q2.getValue());
        entity.setThatUnit(q2.getUnit().getUnitName());
        entity.setThatMeasurementType(q2.getUnit().getMeasurementType());

        entity.setOperation(operation);
        entity.setResultString(resultString);
        entity.setResultValue(resultValue);
        entity.setResultUnit(resultUnit);
        entity.setResultMeasurementType(resultType);

        entity.setError(isError);
        entity.setErrorMessage(errorMessage);

        return entity;
    }

    /** Save error */
    private void saveErrorEntity(QuantityModel<IMeasurable> q1,
                                 QuantityModel<IMeasurable> q2,
                                 String operation,
                                 String errorMessage) {
        try {
            repository.save(buildEntity(q1, q2, operation,
                null, null, null, null, true, errorMessage));
        } catch (Exception ex) {
            log.error("Error saving failed record");
        }
    }
}