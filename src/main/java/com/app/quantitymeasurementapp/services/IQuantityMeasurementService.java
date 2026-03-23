package com.app.quantitymeasurementapp.services;


import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.entity.QuantityMeasurementDTO;
import java.util.*;

public interface IQuantityMeasurementService {
	
	public boolean compare(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO);
	
	public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO);
	
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO);
	
	public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO,QuantityDTO targetQuantityDTO);
	
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO);
	
	public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO,QuantityDTO targetQuantityDTO);
	
	public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO);
	
	public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO,QuantityDTO thatQuantityDTO,QuantityDTO targetQuantityDTO);
	
	public List<QuantityMeasurementDTO> getOperationHistory(String operation);
	
	public List<QuantityMeasurementDTO> getMeasurementsByType(String type);
	
	public long getOperationCount(String operation);
	
	public List<QuantityMeasurementDTO> getErrorHistory();
}
