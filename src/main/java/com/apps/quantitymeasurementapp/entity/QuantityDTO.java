package com.apps.quantitymeasurementapp.entity;

public class QuantityDTO {
	
	public double value;
	public String unit;
	public String measurementType;

	public QuantityDTO(double value, String unit, String measurementType) {
		this.value = value;
		this.unit = unit;
		this.measurementType = measurementType.toUpperCase();
	}

	public double getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public String getMeasurementType() {
		return measurementType.replace("UNIT", "");
	}

	@Override
	public String toString() {
		return "QuantityDTO [value=" + value + ", unit=" + unit + ", measurementType=" + getMeasurementType() + "]";
	}
}