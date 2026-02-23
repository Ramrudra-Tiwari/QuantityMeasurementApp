package quantitymeasurementapp ;

import quantitymeasurementapp.Length.LengthUnit;

public class QuantityMeasurementApp {
	
	public static  class FeetEquality {
	     private final double value;
	     
	     public FeetEquality() {
			this.value = 0;
			}
	     
	     public FeetEquality(double value) {
	    	 if(Double.isNaN(value)) {
	    		 throw new IllegalArgumentException("Invalid Feet value");
	    	 }
	    	 this.value = value;
	     }
	     
	     
	     public double getValue() {
			return value;
		}


		 @Override
	    public boolean equals(Object obj) {
	    	if(this==obj) {
	    		return true;
	    	}
	    	if(obj==null||this.getClass()!=obj.getClass()) {
	    		return false;
	    	}
	    	return Double.compare(this.value, ((FeetEquality) obj).getValue())==0;
		 }
		 
	}
	
	public static class Inches{
		private final  double inche;
		public Inches(double inche) {
			if(Double.isNaN(inche)) {
				throw new IllegalArgumentException("Invalid Inche value");
			}
			this.inche = inche;
		}
		
		public Inches() {
			this.inche = 0;
		}
		public double getInche() {
			return inche;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this==obj) {
	    		return true;
	    	}
	    	if(obj==null||this.getClass()!=obj.getClass()) {
	    		return false;
	    	}
	    	return Double.compare(this.inche, ((Inches) obj).getInche())==0;
		 }
	}
	
	  public static void demonstrateFeetEquality(double v1, double v2) {
	        FeetEquality f1 = new FeetEquality(v1);
	        FeetEquality f2 = new FeetEquality(v2);
	        System.out.println("Feet Equality : "+f1.equals(f2));
	    }

	    public static void demonstrateInchesEquality(double v1, double v2) {
	        Inches i1 = new Inches(v1);
	        Inches i2 = new Inches(v2);
	       System.out.println("Inche Equality : "+i1.equals(i2));
	    }
	    public static boolean demonstrateLengthEquality(Length len1,Length len2) {
	    	return len1.equals(len2);
	    }
	    public static void demonstrateFeetInchComparison() throws InvalidUnitMeasurementException {

	        Length oneFoot = new Length(1, Length.LengthUnit.FEET);
	        Length twelveInches = new Length(12, Length.LengthUnit.INCHES);

	        System.out.println("1 Foot == 12 Inches ? : " 
	                + oneFoot.equals(twelveInches));

	        Length twoFeet = new Length(2, Length.LengthUnit.FEET);
	        Length twentyFourInches = new Length(24, Length.LengthUnit.INCHES);

	        System.out.println("2 Feet == 24 Inches ? : " 
	                + twoFeet.equals(twentyFourInches));

	        Length oneInch = new Length(1, Length.LengthUnit.INCHES);
	        Length oneFootAgain = new Length(1, Length.LengthUnit.FEET);

	        System.out.println("1 Inch == 1 Foot ? : " 
	                + oneInch.equals(oneFootAgain));
	    }
	    
	    public static boolean demonstrateLengthComparison(Length l1,Length l2) {
	    	return l1.compare(l2);
	    }
	    
	    public static Length demonstrateLengthConversion(Double value,LengthUnit unit, LengthUnit toUnit) throws InvalidUnitMeasurementException {
	    	if(value==null) {
	    		throw new IllegalArgumentException("Not Valid Input");
	    	}
	    	Length l = new Length(value,unit);
	    	l=l.convertTo(toUnit);
	    	return l;
	    }
	    
	    public static Length demonstrateLengthConversion(Length l,LengthUnit unit) throws InvalidUnitMeasurementException {
	    	l=l.convertTo(unit);
	    	return l;
	    }
	    
	    public static Length demonstrateLengthAddition(Length len1,Length len2) throws InvalidUnitMeasurementException {
	    	if(len1==null||len2==null) throw new IllegalArgumentException("Invalid Input value");
	    	
	    	len1 = len1.add(len2);
	    	return len1;
	    }
	    
	    public static Length demonstrateLengthAddition(Length len1,Length len2,LengthUnit targetUnit) throws InvalidUnitMeasurementException{
	    	if(len1==null||len2==null||targetUnit==null) {
	    		throw new IllegalArgumentException("Invalid Input");
	    	}
	    	return len1.add(len2, targetUnit);
	    }
	
      public static void main(String[] args) throws InvalidUnitMeasurementException {
    	  
	    demonstrateFeetEquality(1,1);
	    demonstrateInchesEquality(1, 1);
	    demonstrateFeetInchComparison();
	    
	   //Equals demonstration 
	   System.out.println("Are lengths equals : "+ demonstrateLengthEquality(new Length(1,LengthUnit.FEET),new Length(12,Length.LengthUnit.INCHES)));
	   
	   System.out.println("Are Yards and Inches equals : "+demonstrateLengthComparison(new Length(1,LengthUnit.YARD), new Length(36,LengthUnit.INCHES)));
	   
	   System.out.println("Are Centimetre and Inches equals : "+demonstrateLengthComparison(new Length(100,LengthUnit.CENTIMETRE), new Length(39.3701,LengthUnit.INCHES)));
	   
	   System.out.println("Are Feet and Yards equals : "+demonstrateLengthComparison(new Length(3,LengthUnit.FEET), new Length(1,LengthUnit.YARD)));
	   
	   System.out.println("Are Centimetre and Feet : "+demonstrateLengthComparison(new Length(30.48,LengthUnit.CENTIMETRE),new Length(1.0,LengthUnit.FEET)));
   
	   //Conversion demonstration
       System.out.println("Convert Feet To Inche  : "+demonstrateLengthConversion(1.0,LengthUnit.FEET,LengthUnit.INCHES).toString());
       
       System.out.println("Convert Yard To Feet : "+demonstrateLengthConversion(3.0,LengthUnit.YARD,LengthUnit.FEET).toString());
       
       System.out.println("Convert Inches To Yard : "+demonstrateLengthConversion(36.0,LengthUnit.INCHES,LengthUnit.YARD).toString());
       
       System.out.println("Convert Centimetre to Inches : "+demonstrateLengthConversion(1.0, LengthUnit.CENTIMETRE, LengthUnit.INCHES));
       
       System.out.println("Convert Feet To Inches : "+demonstrateLengthConversion(0.0,LengthUnit.FEET, LengthUnit.INCHES));
       
       //Addition demonstration
       System.out.println("Addition of Feet To Feet : "+demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(2.0,LengthUnit.FEET)).toString());
      
       System.out.println("Addition of Feet and Inche : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.FEET),new Length(12.0, LengthUnit.INCHES)).toString());
       
       System.out.println("Addition of Inche and Feet : "+demonstrateLengthAddition(new Length(12.0,LengthUnit.INCHES), new Length(1.0,LengthUnit.FEET)).toString());
       
       System.out.println("Addition of Yards and Feet : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.YARD), new Length(3.0,LengthUnit.FEET)).toString());
       
       System.out.println("Addition of Inches and Yards : "+demonstrateLengthAddition(new Length(36.0,LengthUnit.INCHES), new Length(1.0,LengthUnit.YARD)).toString());
       
       System.out.println("Addition of Centimeter and Inches : "+demonstrateLengthAddition(new Length(2.54,LengthUnit.CENTIMETRE),new Length(1.0,LengthUnit.INCHES)).toString());
       
       System.out.println("Addition of Feet and Inches : "+demonstrateLengthAddition(new Length(5.0,LengthUnit.FEET), new Length(0.0,LengthUnit.INCHES)).toString());
       
       System.out.println("Addition of Feet with minus inche : "+demonstrateLengthAddition(new Length(5.0,LengthUnit.FEET), new Length(-3.0,LengthUnit.FEET)).toString());
       
      //Addition with specific target 
       System.out.println("Addition of Feet with inche to Feet : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.FEET),new Length(12.0,LengthUnit.INCHES),LengthUnit.FEET));
       
       System.out.println("Addition of Feet with Inches to Inches : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.FEET),new Length(12.0,LengthUnit.INCHES),LengthUnit.INCHES));
      
       System.out.println("Addition of Feet with inches to Yards : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.FEET),new Length(12.0,LengthUnit.INCHES),LengthUnit.YARD));
       
       System.out.println("Addition of Yards with Feet to Yards : "+demonstrateLengthAddition(new Length(1.0,LengthUnit.YARD),new Length(3.0,LengthUnit.FEET),LengthUnit.YARD));
       
       System.out.println("Addition of Inches with yards to Feet : "+demonstrateLengthAddition(new Length(36.0,LengthUnit.INCHES),new Length(1.0,LengthUnit.YARD),LengthUnit.FEET));
       
       System.out.println("Addition of Centimeters with Inches to Centimeter : "+demonstrateLengthAddition(new Length(2.54,LengthUnit.CENTIMETRE),new Length(1.0,LengthUnit.INCHES),LengthUnit.CENTIMETRE));
       
       System.out.println("Addition of feet with inhces to yards : "+demonstrateLengthAddition(new Length(5.0,LengthUnit.FEET),new Length(0.0,LengthUnit.INCHES),LengthUnit.YARD));
       
       System.out.println("Addition of Feet with Feet to inches : "+demonstrateLengthAddition(new Length(5.0,LengthUnit.FEET),new Length(-2.0,LengthUnit.FEET),LengthUnit.INCHES));
      }
      
}