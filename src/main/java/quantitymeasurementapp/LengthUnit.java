package quantitymeasurementapp;


public enum LengthUnit{
	FEET(1.0),
    INCHES(1.0 / 12.0),
    YARD(3.0),
    CENTIMETRE(0.0328084);
	 
	 private final double conversion;
	 
	LengthUnit(double conversion) {
		this.conversion = conversion;
	}
	
	public double getConversionFactor() {
		return conversion;
	}
	public double convertToBaseUnit(double value) {
   	 return value*this.getConversionFactor();
    }
}