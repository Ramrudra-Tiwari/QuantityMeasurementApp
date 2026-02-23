package quantitymeasurementapp;


import java.lang.System.Logger.Level;
import java.nio.file.attribute.AclEntryFlag;

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
	
      public static void main(String[] args) throws InvalidUnitMeasurementException {
	    demonstrateFeetEquality(1,1);
	    demonstrateInchesEquality(1, 1);
	    demonstrateFeetInchComparison();
	   System.out.println("Are lengths equals : "+ demonstrateLengthEquality(new Length(1,LengthUnit.FEET),new Length(12,Length.LengthUnit.INCHES)));
	   
	   System.out.println("Are Yards and Inches equals : "+demonstrateLengthComparison(new Length(1,LengthUnit.YARD), new Length(36,LengthUnit.INCHES)));
	   
	   System.out.println("Are Centimetre and Inches equals : "+demonstrateLengthComparison(new Length(100,LengthUnit.CENTIMETRE), new Length(39.3701,LengthUnit.INCHES)));
	   
	   System.out.println("Are Feet and Yards equals : "+demonstrateLengthComparison(new Length(3,LengthUnit.FEET), new Length(1,LengthUnit.YARD)));
	   
	   System.out.println("Are Centimetre and Feet : "+demonstrateLengthComparison(new Length(30.48,LengthUnit.CENTIMETRE),new Length(1.0,LengthUnit.FEET)));
    }
      
}