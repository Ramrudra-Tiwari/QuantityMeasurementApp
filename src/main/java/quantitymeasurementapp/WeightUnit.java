package quantitymeasurementapp;


public enum WeightUnit {
    KG(1.0),
	GRAM(0.001),
	POUND(0.453592);
	
	 private double conversion;
	
     WeightUnit(double conversion) {
		this.conversion = conversion;
	}
      
     public double getConversionFactor() {
    	 return conversion;
     }
     
     public double convertToBaseUnit(double value) {
    	 return value*getConversionFactor();
     }
}