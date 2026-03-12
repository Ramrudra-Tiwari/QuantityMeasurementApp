package quantitymeasurementapp;


public enum VolumneUnit implements IMeasurable{
      LITRE(1.0),
      MILLILITRE(0.001),
      GALLON(3.78541);
	
	private double conversion;
	
	VolumneUnit(double conversion) {
		this.conversion = conversion;
	}

	@Override
	public double getConversionFactor() {
		return conversion;
	}

	@Override
	public double convertToBaseUnit(double value) {
		return value*this.getConversionFactor();
	}

	@Override
	public double convertFromBaseUnit(double value) {
		
		return (VolumneUnit.LITRE.getConversionFactor()*value)/this.getConversionFactor();
	}

	@Override
	public String getUnitName() {
		
		return VolumneUnit.this.name();
	}
      
}
