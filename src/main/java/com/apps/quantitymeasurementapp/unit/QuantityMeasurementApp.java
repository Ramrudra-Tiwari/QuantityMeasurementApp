
package com.apps.quantitymeasurementapp.unit;

import com.apps.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.apps.quantitymeasurementapp.entity.QuantityDTO;
import com.apps.quantitymeasurementapp.exception.InvalidUnitMeasurementException;
import com.apps.quantitymeasurementapp.quantity.Quantity;
import com.apps.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;


public class QuantityMeasurementApp {
	
	//generic method
	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) throws InvalidUnitMeasurementException {
		if(q1==null || q2 ==null || (q1.getUnit().getClass()!=q2.getUnit().getClass())) {
			throw new InvalidUnitMeasurementException("Null Object or illegealarguement exception!");
		}
		return q1.equals(q2);
	}
	
	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> q, U targetUnit) throws InvalidUnitMeasurementException{
		if(q==null || targetUnit ==null ) {
			throw new InvalidUnitMeasurementException("Null Object!");
		}
		return new Quantity<>(q.convertTo(targetUnit), targetUnit);
	}
	
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) throws InvalidUnitMeasurementException{
		if(q1==null || q2 ==null || (q1.getUnit().getClass()!=q2.getUnit().getClass())) {
			throw new InvalidUnitMeasurementException("Null Object or illegealarguement exception!");
		}
		return q1.add(q2);
	}
	
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) throws InvalidUnitMeasurementException{
		if(q1==null || q2 ==null || targetUnit==null || (q1.getUnit().getClass()!=q2.getUnit().getClass())) {
			throw new InvalidUnitMeasurementException("Null Object or illegealarguement exception!");
		}
		return q1.add(q2, targetUnit);
	}
	
	//subtract
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2) throws IllegalArgumentException{
		if(q1==null || q2 ==null || q1.getUnit().getClass()!=q2.getUnit().getClass()) {
			throw new IllegalArgumentException("Both unit are not same");
		}
		
		return q1.subtract(q2);
	}
	
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) throws IllegalArgumentException{
		if(q1==null || q2 ==null || q1.getUnit().getClass()!=q2.getUnit().getClass()) {
			throw new IllegalArgumentException("Both unit are not same");
		}
		
		return q1.subtract(q2, targetUnit);
	}
	//division
	public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) throws IllegalArgumentException{
		if(q1==null || q2 ==null || q1.getUnit().getClass()!=q2.getUnit().getClass()) {
			throw new IllegalArgumentException("Both unit are not same");
		}
		return q1.divide(q2);
	}
	private static QuantityMeasurementApp instance;

	public QuantityMeasurementController controller;

	public IQuantityMeasurementRepository repository;

	private QuantityMeasurementApp() {
		this.repository = QuantityMeasurementCacheRepository.getInstance();
		QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(this.repository);
		this.controller = new QuantityMeasurementController(service);
	}
	
	public static QuantityMeasurementApp getInstance() {
		if (instance == null) {
			instance = new QuantityMeasurementApp();
		}
		return instance;
	}
	
	public static void main(String[] args) throws InvalidUnitMeasurementException {
		QuantityMeasurementApp app = getInstance();
		QuantityDTO feetDTO = new QuantityDTO(12.0, "INCHES", "Length");
	    QuantityDTO inchDTO = new QuantityDTO(12, "FEET", "LengthUnit");
	    QuantityDTO targetDTO = new QuantityDTO(0, "CENTIMETERS", "Length");
	    
	    QuantityDTO result = app.controller.performAddition(feetDTO, inchDTO, targetDTO);
	    System.out.println(result);
	    
	}
}
