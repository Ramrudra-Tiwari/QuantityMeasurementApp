package com.app.quantitymeasurementapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import com.app.quantitymeasurementapp.model.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class QuantityMeasurementDTO {
public double thisValue;
public String thisUnit;
public String thisMeasurementType;
public double thatValue;
public String thatUnit;
public String thatMeasurementType;
public String operation;
public String resultString;
public double resultValue;
public String resultUnit;
public String resultMeasurementType;
public String errorMessage;

@JsonProperty("error")
public boolean error;

	public static QuantityMeasurementDTO from(QuantityMeasurementEntity entity) {
		return new QuantityMeasurementDTO(entity.getThisValue(),entity.getThisUnit(),entity.getThisMeasurementType(),entity.getThatValue(),entity.getThatUnit(),entity.getThatMeasurementType(),
				entity.getOperation(),entity.getResultString(),entity.getResultValue(),entity.getResultUnit(),entity.getResultMeasurementType(),entity.getErrorMessage(),entity.isError());
	}
 
	public QuantityMeasurementEntity toEntity() {
		QuantityDTO thisQuantityDTO = new QuantityDTO(this.thisValue,this.thisUnit,this.thisMeasurementType);
		QuantityDTO thatQuantityDTO = new QuantityDTO(this.thatValue,this.thatUnit,this.thatMeasurementType);
	    if(error) {
	    	return  new QuantityMeasurementEntity(thisQuantityDTO, thatQuantityDTO, this.operation,this.errorMessage,this.error);
	    }
	   QuantityDTO result = new QuantityDTO(this.resultValue,this.resultUnit,this.resultMeasurementType);
	   
	   return new QuantityMeasurementEntity(thisQuantityDTO,thatQuantityDTO,this.operation,result); 
	}
	
	public static List<QuantityMeasurementDTO> fromList(List<QuantityMeasurementEntity> list){
		List<QuantityMeasurementDTO> conversion = new ArrayList<>();
		for(QuantityMeasurementEntity a : list) {
			conversion.add(from(a));
		}
		return conversion;
	}
	
	public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> list){
		List<QuantityMeasurementEntity> conversion = new ArrayList<>();
		for(QuantityMeasurementDTO a : list) {
			conversion.add(a.toEntity());
		}
		return conversion;
	}
}