package quantitymeasurementapp ;

public class Length {
	private static final double EPSILON = 0.0001;
	private double value;
	private LengthUnit len;
    
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
     public LengthUnit getLen() {
    	 return len;
     }
//     To convert value to their base unit  
     
     public boolean compare(Length lengthUnit) {
    	 if(lengthUnit==null)return false;
    	  return Math.abs(this.len.convertToBaseUnit(value) - lengthUnit.len.convertToBaseUnit(lengthUnit.getValue())) < EPSILON;
	}
     
//     overrided .equals methods to check if two units are equal or not 
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
     
     //override tostring method 
     @Override
	public String toString() {
		return "Length [value=" + value + ", len=" + len + "]";
	}
     
    //Conversion of unit to current unit 
	 public Length convertTo(LengthUnit unit) throws InvalidUnitMeasurementException {
    	 double converted = (len.convertToBaseUnit(value))/unit.getConversionFactor();
    	 return new Length(converted,unit);
     }
	 private double convertBaseToTargetUnit(double lengthInInches,LengthUnit targetUnit) {
		 return (lengthInInches*len.getConversionFactor())/targetUnit.getConversionFactor();
	 }
	 
	 //Add To length and convert Unit to current unit
	 public Length add(Length thatLength) throws InvalidUnitMeasurementException {
		 if(thatLength==null)  throw new IllegalArgumentException("Object is null");
		 
		 thatLength = thatLength.convertTo(len);
		 return new Length(value+thatLength.value, len);
	 }
	 private Length addAndConvert(Length length,LengthUnit targetUnit) throws InvalidUnitMeasurementException{
		 if(length==null||targetUnit==null) {
			 throw new IllegalArgumentException("Invalid input");
		 }
		double temp1 = length.convertBaseToTargetUnit(length.getValue(), targetUnit);
		double temp2 = convertBaseToTargetUnit(this.getValue(), targetUnit);
		 return new Length(temp1+temp2,targetUnit);
	 }
	 
	 public Length add(Length length,LengthUnit targetUnit) throws InvalidUnitMeasurementException{
		 return addAndConvert(length, targetUnit);
	 }
	 
	 //Main Method to invoke the methods locally 
     public static void main(String[] args) throws InvalidUnitMeasurementException {
		Length len1 = new Length(1,LengthUnit.FEET);
		Length len2 = new Length(12,LengthUnit.INCHES);
		
		System.out.println("Are Length equals? :"+len1.equals(len2));
	    
		Length len3 = new Length(36,LengthUnit.INCHES);
		Length len4 = new Length(1,LengthUnit.YARD);
		
		System.out.println("Are Inches and Yards equals : "+len3.equals(len4));
		
		Length len5 = new Length(100,LengthUnit.CENTIMETRE);
		Length len6 = new Length(39.3701,LengthUnit.INCHES);
		
		System.out.println("Are Inches and Centimeter equals : "+len5.equals(len6));
     }
}
