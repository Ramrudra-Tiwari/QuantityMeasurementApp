package quantitymeasurementapp;

public class Length {
	private double value;
	private LengthUnit len;
     public enum LengthUnit{
    	 FEET(12.0),
    	 INCHES(1.0);
    	 
    	 private final double conversion;
    	 
    	LengthUnit(double conversion) {
    		this.conversion = conversion;
    	}
    	
    	public double getConversionFactor() {
    		return conversion;
    	}
     }
     
     public Length(double value,LengthUnit len) {
    	 if(Double.isNaN(value)) {
    		 throw new IllegalArgumentException("Invalid Input");
    	 }
    	 this.value = value;
    	 this.len = len;
     }
     
     private double convertToBaseUnit() {
    	 return value*len.getConversionFactor();
     }
     
     public boolean compare(Length lengthUnit) {
		if(convertToBaseUnit()==lengthUnit.convertToBaseUnit()) {
			return true;
		}
		return false;
	}
     
     @Override
    public boolean equals(Object obj) {
        if(this==obj) {
        	return true;
        }
        if(obj==null||this.getClass()!=obj.getClass()) {
        	return false;
        }
        Length l = (Length)obj;
        return this.compare(l);
    }
     
     public static void main(String[] args) {
		Length len1 = new Length(1,Length.LengthUnit.INCHES);
		Length len2 = new Length(12,Length.LengthUnit.INCHES);
		
		System.out.println("Are Length equals? :"+len1.equals(len2));
	}
}