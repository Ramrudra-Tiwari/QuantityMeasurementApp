package com.app.quantitymeasurementapp.services;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.entity.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.model.Quantity;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.model.QuantityModel;
import com.app.quantitymeasurementapp.repository.QuantityMeasurentRepository;
import com.app.quantitymeasurementapp.unit.IMeasurable;
import com.app.quantitymeasurementapp.unit.LengthUnit;
import com.app.quantitymeasurementapp.unit.Temperature;
import com.app.quantitymeasurementapp.unit.VolumneUnit;
import com.app.quantitymeasurementapp.unit.WeightUnit;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService{
	
	private static final Logger logger = Logger.getLogger(QuantityMeasurementServiceImpl. class.getName());
    
	private QuantityMeasurentRepository repository;
	
	public QuantityMeasurementServiceImpl(QuantityMeasurentRepository repository) {
		this.repository = repository;
	}
	
	private enum Operation{
		COMPARISION,CONVERSION,ARITHMETIC;
	}
	@Override
	public boolean compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		 QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		 QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		 validateArithmeticOperation(q1, q2);
		 Quantity<IMeasurable> q3 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		 Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q2.getValue(),q2.getUnit());
		 
		 boolean result = q3.compare(q4);
		 repository.save(new QuantityMeasurementEntity(thisQuantityDTO,thatQuantityDTO,Operation.COMPARISION.toString(),result?"EQUAL":"NOT EQUAL"));
		 return result;
	}

	@Override
	public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		try {
		QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		validateArithmeticOperation(q1, q2);
		Quantity<IMeasurable> q3 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		q4 = q3.convertTo(q4);
		
		QuantityDTO q5 = new QuantityDTO(q4.getValue(),q4.getUnit().getUnitName(),q4.getUnit().getClass().getSimpleName());
		repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO, Operation.CONVERSION.toString(),q5));
		return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"SUCCESS",q5.getValue(),q5.getUnit(),q5.getMeasurementType(),"NoError", false);}
		catch(Exception e) {
		return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"FAIL",0,null,null,e.getMessage(),true);

		}
	}

	@Override
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		try {
		QuantityModel<?> q1 =getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		validateArithmeticOperation(q1, q2);
		Quantity<IMeasurable> q3 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		
		q4 = q3.add(q4);
		
		QuantityDTO q5 =  new QuantityDTO(q4.getValue(),q3.getUnit().getUnitName(),q3.getUnit().getClass().getSimpleName());
	    repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO, Operation.ARITHMETIC.toString(),q5));
		 return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"SUCCESS",q5.getValue(),q5.getUnit(),q5.getMeasurementType(),"NoError", false);}
	    catch(Exception e) {
	     return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"FAIL",0,null,null,e.getMessage(),true);

	}
	}

	@Override
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetQuantityDTO) {
		try {
		QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		QuantityModel<?> q3 = getQuantityInstance(targetQuantityDTO);
		validateArithmeticOperation(q1, q2);
		validateArithmeticOperation(q2, q3);
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		Quantity<IMeasurable> q5 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		
		q5 = q4.add(q5,q3.getUnit());
		QuantityDTO q6 = new QuantityDTO(q5.getValue(),q5.getUnit().getUnitName(),q5.getUnit().getClass().getSimpleName());
		
		repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO,Operation.ARITHMETIC.toString(),q6));
		
		 return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"SUCCESS",q6.getValue(),q6.getUnit(),q6.getMeasurementType(),"NoError", false);}
	    catch(Exception e) {
	     return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"FAIL",0,null,null,e.getMessage(),true);

    	}
	}

	@Override
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		try {
		QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		validateArithmeticOperation(q1, q2);
		Quantity<IMeasurable> q3 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		
		q4=q3.subtract(q4);
		
		QuantityDTO q5 = new QuantityDTO(q4.getValue(),q4.getUnit().getUnitName(),q4.getUnit().getClass().getSimpleName());
		repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO, Operation.ARITHMETIC.toString(),q5));
		return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"SUCCESS",q5.getValue(),q5.getUnit(),q5.getMeasurementType(),"NoError", false);}
	   catch(Exception e) {
	    return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"FAIL",0,null,null,e.getMessage(),true);

	}
	}

	@Override
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetQuantityDTO) {
		try {
		QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		QuantityModel<?> q3 = getQuantityInstance(targetQuantityDTO);
		validateArithmeticOperation(q1, q2);
		validateArithmeticOperation(q2, q3);
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		Quantity<IMeasurable> q5 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		q5 = q4.subtract(q5,q3.getUnit());
		
		QuantityDTO q6 = new QuantityDTO(q5.getValue(),q5.getUnit().getUnitName(),q5.getUnit().getClass().getSimpleName());
		
		repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO,Operation.ARITHMETIC.toString(),q6));
		 return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"SUCCESS",q6.getValue(),q6.getUnit(),q6.getMeasurementType(),"NoError", false);}
       catch(Exception e) {
         return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.ARITHMETIC.toString(),"FAIL",0,null,null,e.getMessage(),true);

	}
	}

	@Override
	public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		try {
		QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		validateArithmeticOperation(q1, q2);
		Quantity<IMeasurable> q3 = new Quantity<IMeasurable>(q1.getValue(), q1.getUnit());
		Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q2.getValue(),q2.getUnit());
		
		q3 = q3.division(q4);
		QuantityDTO q5 = new QuantityDTO(q3.getValue(),q3.getUnit().getUnitName(),q3.getUnit().getClass().getSimpleName());
		
		repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO, Operation.ARITHMETIC.toString(),q5));
		 return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"SUCCESS",q5.getValue(),q5.getUnit(),q5.getMeasurementType(),"NoError", false);}
    	catch(Exception e) {
	     return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"SUCCESS",0,null,null,e.getMessage(),true);

	}
	}

	@Override
	public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetQuantityDTO) {
		try {
		 QuantityModel<?> q1 = getQuantityInstance(thisQuantityDTO);
		 QuantityModel<?> q2 = getQuantityInstance(thatQuantityDTO);
		 QuantityModel<?> q3 = getQuantityInstance(targetQuantityDTO);
		 validateArithmeticOperation(q1, q2);
		 validateArithmeticOperation(q2, q3);
		 Quantity<IMeasurable> q4 = new Quantity<IMeasurable>(q1.getValue(),q1.getUnit());
		 Quantity<IMeasurable> q5 = new Quantity<IMeasurable>(q2.getValue(), q2.getUnit());
		 
		 q4 = q4.division(q5,q3.getUnit());
		 
		 QuantityDTO q6 = new QuantityDTO(q4.getValue(),q4.getUnit().getUnitName(),q4.getUnit().getClass().getSimpleName());
		 
		 repository.save(new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO,Operation.ARITHMETIC.toString(),q6));
		 
		 return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"SUCCESS",q6.getValue(),q6.getUnit(),q6.getMeasurementType(),"NoError", false);}
        catch(Exception e) {
         return  new QuantityMeasurementDTO(thisQuantityDTO.getValue(),thisQuantityDTO.getUnit(),thisQuantityDTO.getMeasurementType(),thatQuantityDTO.getValue(),thatQuantityDTO.getUnit(),thatQuantityDTO.getMeasurementType(),Operation.CONVERSION.toString(),"SUCCESS",0,null,null,e.getMessage(),true);

	}
	}
    private QuantityModel<?> getQuantityInstance(QuantityDTO dto){
    	switch(dto.getMeasurementType()) {
    	case "VOLUME":
    		return new QuantityModel<>(dto.getValue(),VolumneUnit.valueOf(dto.getUnit()));
    	case "WEIGHT":
    		return new QuantityModel<>(dto.getValue(),WeightUnit.valueOf(dto.getUnit()));
    	case "LENGTH":
    		return new QuantityModel<>(dto.getValue(),LengthUnit.valueOf(dto.getUnit()));
    	case "TEMPERATURE":
    		return new QuantityModel<>(dto.getValue(),Temperature.valueOf(dto.getUnit()));
    	default:
    		throw new IllegalArgumentException("The Unit Does Not Exists");
    	}
    }
    private void  validateArithmeticOperation(QuantityModel<?> thisQuantityModel,QuantityModel<?> thatQuantityModel) {
    	if(Double.isInfinite(thisQuantityModel.getValue())||Double.isNaN(thisQuantityModel.getValue())||Double.isInfinite(thatQuantityModel.getValue())||Double.isNaN(thatQuantityModel.getValue())) {
    		throw new IllegalArgumentException("Invalid Value");
    	}
    	if(thisQuantityModel.getUnit().getClass()!=thatQuantityModel.getUnit().getClass()) {
    		throw new IllegalArgumentException("Unit mismatch");
    	}
    }

	@Override
	public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
		List<QuantityMeasurementEntity> list = repository.findByOperation(operation);
		List<QuantityMeasurementDTO> result = new ArrayList<>();
		for(QuantityMeasurementEntity a : list) {
			result.add(QuantityMeasurementDTO.from(a));
		}
		return result;
	}

	@Override
	public List<QuantityMeasurementDTO> getMeasurementsByType(String type) {
		List<QuantityMeasurementEntity> list = repository.findByThisMeasurementType(type);
		List<QuantityMeasurementDTO> result = new ArrayList<>();
		for(QuantityMeasurementEntity a : list) {
			result.add(QuantityMeasurementDTO.from(a));
		}
		return result;
	}

	@Override
	public long getOperationCount(String operation) {
	   return repository.countByOperationAndIsErrorFalse(operation);
	}

	@Override
	public List<QuantityMeasurementDTO> getErrorHistory() {
		List<QuantityMeasurementEntity> list = repository.findByIsErrorTrue();
		List<QuantityMeasurementDTO> result = new ArrayList<>();
		for(QuantityMeasurementEntity a : list) {
			result.add(QuantityMeasurementDTO.from(a));
		}
		return result;
	}
}