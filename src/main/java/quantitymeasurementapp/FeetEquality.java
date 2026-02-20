package quantitymeasurementapp;

public class FeetEquality {

	private final double value;
	
	public FeetEquality(double value) {
		if(Double.isNaN(value)) {
			throw new IllegalArgumentException("Invalid input!");
		}
		this.value = value;
	}
	public Double getValue() {
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		
		if(obj==null || obj.getClass()!=this.getClass()) {
			return false;
		}
		return Double.compare(this.value, ((FeetEquality)obj).getValue())==0;
	}
}