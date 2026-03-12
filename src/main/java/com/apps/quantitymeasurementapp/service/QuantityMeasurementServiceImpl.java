package com.apps.quantitymeasurementapp.service;

import com.apps.quantitymeasurementapp.entity.QuantityDTO;
import com.apps.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurementapp.entity.QuantityModel;
import com.apps.quantitymeasurementapp.exception.CategoryMismatchException;
import com.apps.quantitymeasurementapp.exception.InvalidUnitException;
import com.apps.quantitymeasurementapp.exception.InvalidUnitMeasurementException;
import com.apps.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.apps.quantitymeasurementapp.quantity.Quantity;
import com.apps.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurementapp.unit.IMeasurable;
import com.apps.quantitymeasurementapp.unit.LengthUnit;
import com.apps.quantitymeasurementapp.unit.TemperatureUnit;
import com.apps.quantitymeasurementapp.unit.VolumeUnit;
import com.apps.quantitymeasurementapp.unit.WeightUnit;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService{
	
	private IQuantityMeasurementRepository repository;
	
	//constructor
	public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		// 1. Map
		QuantityModel<IMeasurable> m1 = mapToModel(thisQuantityDTO);
		QuantityModel<IMeasurable> m2 = mapToModel(thatQuantityDTO);
		
		//validate
		validateModels(m1, m2);
		
		// 3. Create Domain Objects
	    Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
	    Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());
	    
	    // 4. Use the equals method from Quantity.java
	    boolean isEqual = q1.equals(q2);
	    
	    // 5. Save to Repository (Audit Trail)
	    QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
	            thisQuantityDTO.getValue(), thisQuantityDTO.getUnit(), thisQuantityDTO.getMeasurementType(),
	            thatQuantityDTO.getValue(), thatQuantityDTO.getUnit(), thatQuantityDTO.getMeasurementType(),
	            "COMPARE",
	            isEqual ? 1.0 : 0.0, // Storing result as 1 for true, 0 for false
	            "BOOLEAN",
	            thisQuantityDTO.getMeasurementType()
	    );
	    repository.save(entity);
				
        return isEqual;
	}
	
	@Override
	public QuantityDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, "CONVERT");
	}

	@Override
	public QuantityDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, "ADD");
	}

	@Override
	public QuantityDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
		return executeArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, "ADD_TO_TARGET");
	}

	@Override
	public QuantityDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, "SUBTRACT");
	}

	@Override
	public QuantityDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {;
		return executeArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, "SUBTRACT_TO_TARGET");
	}

	@Override
	public double divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		return executeArithmetic(thatQuantityDTO, thisQuantityDTO, null, "DIVIDE").getValue();
	}
	
	  /**
     * Helper to map DTO (Strings) to Model (Actual Unit Enums)
     */
    private QuantityModel<IMeasurable> mapToModel(QuantityDTO dto) {
        String type = dto.getMeasurementType().toUpperCase();
        String unitName = dto.getUnit().toUpperCase();
        IMeasurable unit;

        try {
	        	switch (type.toUpperCase()) {
	            case "LENGTH": unit = LengthUnit.valueOf(unitName); break;
	            case "VOLUME": unit = VolumeUnit.valueOf(unitName); break;
	            case "WEIGHT": unit = WeightUnit.valueOf(unitName); break;
	            case "TEMPERATURE": unit = TemperatureUnit.valueOf(unitName); break;
	            default: throw new InvalidUnitMeasurementException("Invalid Measurement Category: " + type);
	        }
        }
        catch(IllegalArgumentException e) {
        		throw new InvalidUnitException("Unit '" + unitName + "' is not valid for " + type);
        	}
        return new QuantityModel<>(dto.getValue(), unit);
    }
    

    /**
     * Validation logic as requested in the flow diagram
     */
    private void validateModels(QuantityModel<?> m1, QuantityModel<?> m2) {
        if (m1 == null || m2 == null) {
        		throw new QuantityMeasurementException("Measurement operands cannot be null"); 
        }
        
        if (m1.getUnit().getClass() != m2.getUnit().getClass()) {
        		throw new CategoryMismatchException("Incompatible types: " + m1.getUnit().getClass().getSimpleName() + " vs " + m2.getUnit().getClass().getSimpleName());        
        	}
        
        if (!Double.isFinite(m1.getValue()) || !Double.isFinite(m2.getValue())) {
        		throw new QuantityMeasurementException("Invalid numeric value provided");
        	}
    }
    
    /**
     * This will helper method reuse for all method 
     */
    
    private QuantityDTO executeArithmetic(QuantityDTO d1, QuantityDTO d2, QuantityDTO target, String opType) {
		// 1. Map
		QuantityModel<IMeasurable> m1 = mapToModel(d1);
		QuantityModel<IMeasurable> m2 = mapToModel(d2);
		QuantityModel<IMeasurable> mT = (target != null) ? mapToModel(target) : null;

		// 2. Validate
		validateModels(m1, m2);
		if (mT != null)
			validateModels(m1, mT);

		// 3. Domain Call (Quantity.java handles the actual Math)
		Quantity<IMeasurable> q1 = new Quantity<>(m1.getValue(), m1.getUnit());
		Quantity<IMeasurable> q2 = new Quantity<>(m2.getValue(), m2.getUnit());

		Quantity<IMeasurable> result;
		if (opType.contains("ADD")) {
			result = (mT != null) ? q1.add(q2, mT.getUnit()) : q1.add(q2);
		}
		else if (opType.contains("SUBTRACT")) {
			result = (mT != null) ? q1.subtract(q2, mT.getUnit()) : q1.subtract(q2);
		}
		else{
			double value = q1.divide(q1);
			result = new Quantity<IMeasurable>(value, q1.getUnit());
		}

		// 4. Extract & Save (Persistence)
		double resVal = result.getValue();
		String resUnit = result.getUnit().toString();

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
				d1.getValue(), 
				d1.getUnit(),
				d1.getMeasurementType(), 
				d2.getValue(), d2.getUnit(), 
				d2.getMeasurementType(), 
				opType, 
				resVal, 
				resUnit,
				d1.getMeasurementType());
		
		repository.save(entity);

		// 5. Return
		return new QuantityDTO(resVal, resUnit, d1.getMeasurementType());
	}
}