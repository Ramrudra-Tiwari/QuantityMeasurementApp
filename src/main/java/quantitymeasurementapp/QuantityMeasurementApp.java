package quantitymeasurementapp ;
public class QuantityMeasurementApp {

    public static<T extends IMeasurable> boolean demonstrateEquality(Quantity<T> quantity1,Quantity<T> quantity2) {
    	if(quantity1.getUnit().getClass()!=quantity2.getUnit().getClass()) throw new IllegalArgumentException("Invalid Unit does Not Match");
    	return quantity1.equals(quantity2);
    }
    
    public static <T extends IMeasurable> Quantity<T> demonstrateConversion(Quantity<T> quantity1,T targetUnit){
        if(quantity1.getUnit().getClass()!=targetUnit.getClass()) throw new IllegalArgumentException("Invalid Unit does Not match");
    	return quantity1.convertTo(targetUnit);
    }
    
    public static <T extends IMeasurable> Quantity<T> demonstrateAddition(Quantity<T> quantity1, Quantity<T> quantity2){
    	if(quantity1.getUnit().getClass()!=quantity2.getUnit().getClass()) throw new IllegalArgumentException("Invalid Unit does Not Match");
    	return quantity1.add(quantity2);
    }
    
    public static <T extends IMeasurable> Quantity<T> demonstrateAddition(Quantity<T> quantity1, Quantity<T> quantity2,T targetUnit){
    	if(targetUnit.getClass()!=quantity1.getUnit().getClass()||targetUnit.getClass()!=quantity2.getUnit().getClass()) throw new IllegalArgumentException("Invalid Unit Does Not Match");
    	return quantity1.add(quantity2,targetUnit);
    }
    
    public static void main(String[] args) {
    	
//    	Length Unit
		Quantity<LengthUnit> len1 = new Quantity<LengthUnit>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> len2 = new Quantity<LengthUnit>(1.0,LengthUnit.FEET);
		System.out.println("Are The len1 and Len2 equals : "+demonstrateEquality(len1,len2));
		
		System.out.println("Conversion Inche To Feet : "+len2.convertTo(LengthUnit.INCHES));
		System.out.println("Addition of feet and inches : "+len2.add(len1,LengthUnit.FEET));
		
//		Weight Unit
	    Quantity<WeightUnit> w1 = new Quantity<WeightUnit>(1.0, WeightUnit.KG);
	    Quantity<WeightUnit> w2 = new Quantity<WeightUnit>(1000.0,WeightUnit.GRAM);
	    System.out.println("Are the w1 and w2 equals : "+demonstrateEquality(w1, w2));
	    
	    System.out.println("Convert Kg To Gram : "+demonstrateConversion(w1,WeightUnit.GRAM));
	    System.out.println("Addition of Kg and Gram : "+demonstrateAddition(w1, w2,WeightUnit.KG));
	    
	}
}