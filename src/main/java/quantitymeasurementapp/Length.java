package quantitymeasurementapp;


public class Length {
	private static final double EPSILON = 0.0001;
	private double value;
	private LengthUnit len;
     public enum LengthUnit{
    	 FEET(12.0),
    	 INCHES(1.0),
    	 YARD(36.0),
    	 CENTIMETRE(0.393701);
    	 
    	 private final double conversion;
    	 
    	LengthUnit(double conversion) {
    		this.conversion = conversion;
    	}
    	
    	public double getConversionFactor() {
    		return conversion;
    	}
     }
     public Length() {};
   
     public Length(double value,LengthUnit len) throws InvalidUnitMeasurementException {
    	 if(Double.isNaN(value)||Double.isInfinite(value)) {
    		 throw new IllegalArgumentException("Invalid Input");
    	 }
    	 if(len==null) {
    		 throw new InvalidUnitMeasurementException("Unit is null");
    	 }
    	 this.value = value;
    	 this.len = len;
     }
     
     public double getValue() {
    	 return value;
     }
     private double convertToBaseUnit() {
    	 return value*len.getConversionFactor();
     }
    
     public boolean compare(Length lengthUnit) {
    	 if(lengthUnit==null)return false;
    	  return Math.abs(this.convertToBaseUnit() - lengthUnit.convertToBaseUnit()) < EPSILON;
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
     
     
     @Override
	public String toString() {
		return "Length [value=" + value + ", len=" + len + "]";
	}

	 public Length convertTo(LengthUnit unit) throws InvalidUnitMeasurementException {
    	 double converted = (this.value*len.getConversionFactor())/unit.getConversionFactor();
    	 return new Length(converted,unit);
     }
	 
	 
     public static void main(String[] args) throws InvalidUnitMeasurementException {
		Length len1 = new Length(1,Length.LengthUnit.FEET);
		Length len2 = new Length(12,Length.LengthUnit.INCHES);
		
		System.out.println("Are Length equals? :"+len1.equals(len2));
	    
		Length len3 = new Length(36,LengthUnit.INCHES);
		Length len4 = new Length(1,LengthUnit.YARD);
		
		System.out.println("Are Inches and Yards equals : "+len3.equals(len4));
		
		Length len5 = new Length(100,LengthUnit.CENTIMETRE);
		Length len6 = new Length(39.3701,LengthUnit.INCHES);
		
		System.out.println("Are Inches and Centimeter equals : "+len5.equals(len6));
     }
}
