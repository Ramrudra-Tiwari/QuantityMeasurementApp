
package com.apps.quantitymeasurementapp;


import com.apps.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.apps.quantitymeasurementapp.entity.QuantityDTO;
import com.apps.quantitymeasurementapp.exception.InvalidUnitMeasurementException;
import com.apps.quantitymeasurementapp.quantity.Quantity;
import com.apps.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurementapp.repository.QuantityMeasurementDatabaseRepository;
import com.apps.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurementapp.unit.IMeasurable;
import com.apps.quantitymeasurementapp.unit.LengthUnit;
import com.apps.quantitymeasurementapp.unit.VolumeUnit;
import com.apps.quantitymeasurementapp.unit.WeightUnit;


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

	private QuantityMeasurementApp(){
		this.repository = QuantityMeasurementDatabaseRepository.getInstance();
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
		QuantityDTO feetDTO = new QuantityDTO(12.0, "INCHES", "LengthUnit");
	    QuantityDTO inchDTO = new QuantityDTO(12, "FEET", "LengthUnit");
	    QuantityDTO targetDTO = new QuantityDTO(0, "CENTIMETERS", "LengthUnit");
	    
	    QuantityDTO result = app.controller.performAddition(feetDTO, inchDTO, targetDTO);
	    System.out.println(result);
	    
	    
	    
	    QuantityDTO q1 = new QuantityDTO(1.0, WeightUnit.KILOGRAM);
	    QuantityDTO q2 = new QuantityDTO(2000, WeightUnit.GRAM);
	    System.out.println(
	    			app.controller.performAddition(q1, q2)
	    		);
	    
	    
	    
	    
	    System.out.println(
	    		app.controller.performComparison(
	                    new QuantityDTO(1.0, VolumeUnit.LITRE),
	                    new QuantityDTO(1000.0, VolumeUnit.MILLILITRE))
	    		);
	    
	    QuantityDTO source = new QuantityDTO(1.0, LengthUnit.FEET);
        QuantityDTO target = new QuantityDTO(0.0, LengthUnit.INCHES);

        QuantityDTO resultd = app.controller.performConversion(source, target);
        System.out.println(resultd);
        
        QuantityDTO q11 = new QuantityDTO(2.0, VolumeUnit.LITRE);
        QuantityDTO q22 = new QuantityDTO(1000.0, VolumeUnit.MILLILITRE);
        System.out.println(
        			app.controller.performDivision(q11, q22)
        		);
	}
}
