package quantitymeasurementapp    ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import quantitymeasurementapp .InvalidUnitMeasurementException;
import quantitymeasurementapp .Length;
import quantitymeasurementapp .Length.LengthUnit;
import quantitymeasurementapp .QuantityMeasurementApp;
import quantitymeasurementapp .QuantityMeasurementApp.FeetEquality;
import quantitymeasurementapp .QuantityMeasurementApp.Inches;

public class QuantityMeasurementAppTest {
		
		QuantityMeasurementApp.FeetEquality feet1;
		QuantityMeasurementApp.FeetEquality feet2;
		QuantityMeasurementApp.Inches inche1;
		QuantityMeasurementApp.Inches inche2;
		Length len1;
		Length len2;
	
	    @Test
	    public void testFeetEquals() {
	    	feet1 = new FeetEquality(1);
	    	feet2 = new FeetEquality(1);
	    	assertTrue(feet1.equals(feet2));
	    }
	    
	    @Test
	    public void testFeetDifferent() {
	    	feet1 = new FeetEquality(1);
	    	feet2 = new FeetEquality(2);
	    	assertTrue(!feet1.equals(feet2));
	    }
	    
	    @Test
	    public void testFeetNull() {
	    	feet1 = new FeetEquality(1);
	    	feet2 = null;
	    	assertTrue(!feet1.equals(feet2));
	    }
	    
	    @Test
	    public void testFeetSameRefferencet() {
	    	feet1 = new FeetEquality(1);
	    	
	    	assertTrue(feet1.equals(feet1));
	    }
	    
	    @Test
	    public void testFeetEmptyVariable() {
	    	feet1 = new FeetEquality(1);
	    	feet2 = new FeetEquality();
	    	assertTrue(!feet1.equals(feet2));
	    }
	    
	    @Test
	    public void testIncheEmptyVariable() {
	    	inche1 = new Inches(1);
	    	inche2 = new Inches();
	    	assertTrue(!inche1.equals(inche2));
	    }
	    
	    @Test
	    public void testIncheSameRefference() {
	    	inche1 = new Inches(1);
	    	
	    	assertTrue(inche1.equals(inche1));
	    }
	
	    @Test
	    public void testIncheNull() {
	    	inche1 = new Inches(1);
	    	inche2 = null;
	    	assertTrue(!inche1.equals(inche2));
	    }
	    
	    @Test
	    public void testIncheDifferent() {
	    	inche1 = new Inches(1);
	    	inche2 = new Inches(2);
	    	assertTrue(!inche1.equals(inche2));
	    }
	    
	    @Test
	    public void testInchesEqual() {
	    	inche1 = new Inches(1);
	    	inche2 = new Inches(1);
	    	assertTrue(inche1.equals(inche2));
	    }

	    @Test
	    public void testFeetEquality() throws InvalidUnitMeasurementException {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(2,LengthUnit.FEET);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testIncheEquality() throws InvalidUnitMeasurementException {
	    	len1 = new Length(2,LengthUnit.INCHES);
	    	len2 = new Length(2,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testFeetIncheComparision() throws InvalidUnitMeasurementException {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(24,LengthUnit.INCHES);
	    	assertTrue(len1.compare(len2));
	    }
	    
	    @Test
	    public void testFeetInEquality() throws InvalidUnitMeasurementException {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(24,LengthUnit.FEET);
	    	assertTrue(!len1.equals(len2));
	    }
	    
	    @Test
	    public void testIncheInEquality() throws InvalidUnitMeasurementException {
	    	len1 = new Length(2,LengthUnit.INCHES);
	    	len2 = new Length(24,LengthUnit.INCHES);
	    	assertTrue(!len1.equals(len2));
	    }
	   
	    @ParameterizedTest
	    @ValueSource(doubles= {1,4,5,6,5})
	    public void multipleFeetcomparison(double feet) throws InvalidUnitMeasurementException {
	    	len1 = new Length(feet,LengthUnit.FEET);
	    	len2 = new Length(feet*12,LengthUnit.INCHES);
	    	assertTrue(len1.compare(len2));
	    }
	    
	    @Test
	    public void testEqualityYardToYard() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(1,LengthUnit.YARD);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testInEqualityYardToYard() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(2,LengthUnit.YARD);
	    	assertTrue(!len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityYardToFeetEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(3,LengthUnit.FEET);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityFeetToYardEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(3,LengthUnit.FEET);
	    	len2 = new Length(1,LengthUnit.YARD);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityYardToInchesEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(36,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityInchesToYardEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(36,LengthUnit.INCHES);
	    	len2 = new Length(1,LengthUnit.YARD);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityYardToFeetNonEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(2,LengthUnit.FEET);
	    	assertTrue(!len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualitycentimetersToInchesEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.CENTIMETRE);
	    	len2 = new Length(0.393701,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualitycentimetersToFeetNonEquivalentValue() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.CENTIMETRE);
	    	len2 = new Length(1,LengthUnit.FEET);
	    	assertTrue(!len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityMultiUnitTransitiveProperty() throws InvalidUnitMeasurementException {
	    	len1 = new Length(1,LengthUnit.YARD);
	    	len2 = new Length(3,LengthUnit.FEET);
	    	Length len3 = new Length(36,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2)&&len2.equals(len3));
	    }
	    
	    @Test
	    public void testEqualityYardWithNullUnit() throws InvalidUnitMeasurementException {
	    	len1= new Length(1,LengthUnit.YARD);
	    	assertTrue(!len1.equals(null));
	    }
	    
	    @Test
	    public void testEqualityYardSameReference() throws InvalidUnitMeasurementException {
	    	len1= new Length(1,LengthUnit.YARD);
	    	len2 = len1;
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testEqualityYardNullComparison() throws InvalidUnitMeasurementException {
	    	len1= new Length(1,LengthUnit.YARD);
	    	assertTrue(!len1.compare(null));
	    }
	    @Test
	    public void testEqualityCentimetersWithNullUnit() throws InvalidUnitMeasurementException{
	       assertThrows(InvalidUnitMeasurementException.class,()->{
	    	   len1 = new Length(1,null);
	       });
	    }
	    @Test
	    public void testEqualityCentimetersSameReference() throws InvalidUnitMeasurementException{
	    	len1 = new Length(1,LengthUnit.CENTIMETRE);
	    	len2 = len1;
	    	assertTrue(len1.equals(len2));
	    }
	  
	@Test
	   public void testEqualityAllUnitsComplexScenario()throws InvalidUnitMeasurementException{
		    len1 = new Length(2,LengthUnit.YARD);
	    	len2 = new Length(6,LengthUnit.FEET);
	    	Length len3 = new Length(72,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2)&&len1.equals(len3));
	   }
	
	@Test
	public void testConversionFeetToInches() throws InvalidUnitMeasurementException {
		assertEquals(12.0,QuantityMeasurementApp.demonstrateLengthConversion(1.0,LengthUnit.FEET,LengthUnit.INCHES).getValue());
	}
	
	@Test
	public void testConversionInchesToFeet() throws InvalidUnitMeasurementException{
		assertEquals(2.0,QuantityMeasurementApp.demonstrateLengthConversion(24.0,LengthUnit.INCHES,LengthUnit.FEET).getValue());
	}
	 @Test
	 public void testConversionYardToInches()throws InvalidUnitMeasurementException{
		 assertEquals(36.0,QuantityMeasurementApp.demonstrateLengthConversion(1.0,LengthUnit.YARD,LengthUnit.INCHES).getValue());	
		 }
	 @Test
	 public void testConversionInchesToYards()throws InvalidUnitMeasurementException {
		 assertEquals(2.0,QuantityMeasurementApp.demonstrateLengthConversion(72.0,LengthUnit.INCHES,LengthUnit.YARD).getValue());
	 }
     @Test
     public void testConversionCentimetreToInches() throws InvalidUnitMeasurementException{
    	 assertEquals(1,QuantityMeasurementApp.demonstrateLengthConversion(2.54,LengthUnit.CENTIMETRE,LengthUnit.INCHES).getValue(),0.0001);
     }
     @Test
     public void testConversionFeetToYard() throws InvalidUnitMeasurementException{
    	 assertEquals(2.0,QuantityMeasurementApp.demonstrateLengthConversion(6.0,LengthUnit.FEET,LengthUnit.YARD).getValue());
     }
     @Test
     public void testConversionRoundTripPreservesValue() throws InvalidUnitMeasurementException{
    	 assertEquals(12,QuantityMeasurementApp.demonstrateLengthConversion(QuantityMeasurementApp.demonstrateLengthConversion(12.0,LengthUnit.INCHES, LengthUnit.FEET).getValue(), LengthUnit.FEET, LengthUnit.INCHES).getValue());
     }
     @Test
     public void testConversionZeroValue() throws InvalidUnitMeasurementException{
    	 assertEquals(0.0,QuantityMeasurementApp.demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES).getValue());
     }
     @Test
     public void testConversionNegativeValue() throws InvalidUnitMeasurementException{
    	 assertEquals(-12, QuantityMeasurementApp.demonstrateLengthConversion(-1.0,LengthUnit.FEET, LengthUnit.INCHES).getValue());
     }
     @Test
     public void testConversionInvalidUnitThrows() throws InvalidUnitMeasurementException{
    	 assertThrows(IllegalArgumentException.class,()->{
    		QuantityMeasurementApp.demonstrateLengthConversion(null, Length.LengthUnit.FEET,Length.LengthUnit.INCHES); 
    	 });
     }
    @Test
     public void testConversionNaNOrInfiniteThrows() throws InvalidUnitMeasurementException{
    	assertThrows(IllegalArgumentException.class,()->{
    		QuantityMeasurementApp.demonstrateLengthConversion(Double.NaN,LengthUnit.CENTIMETRE,LengthUnit.INCHES);
    	});
    }
    @Test
    void testConversion_PrecisionTolerance() throws InvalidUnitMeasurementException {
        assertEquals(1.0,QuantityMeasurementApp.demonstrateLengthConversion(2.54, LengthUnit.CENTIMETRE,LengthUnit.INCHES).getValue(), 1e-6);
    } 
    
    //Test Methods For addition logic 
    
    @Test
    public void testAdditionSameUnitFeetPlusFeet() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.FEET);
    	len2 = new Length(2.0,LengthUnit.FEET);
    	assertEquals(3.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionSameUnitInchPlusInch() throws InvalidUnitMeasurementException{
    	len1 = new Length(6.0,LengthUnit.INCHES);
    	len2 = new Length(6.0,LengthUnit.INCHES);
    	assertEquals(12.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionCrossUnitFeetPlusInches() throws InvalidUnitMeasurementException {
    	len1 = new Length(1.0,LengthUnit.FEET);
    	len2 = new Length(12.0,LengthUnit.INCHES);
    	assertEquals(2.0,len1.add(len2).getValue());
    }
    @Test
    public void testAdditionCrossUnitInchePlusFeet() throws InvalidUnitMeasurementException{
    	len1 = new Length(12.0,LengthUnit.INCHES);
    	len2 = new Length(1.0,LengthUnit.FEET);
    	assertEquals(24.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionCrossUnitYardPlusFeet() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.YARD);
    	len2 = new Length(3.0,LengthUnit.FEET);
    	assertEquals(2.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionCrossUnitCentimeterPlusInch() throws InvalidUnitMeasurementException{
    	len1 = new Length(2.54,LengthUnit.CENTIMETRE);
    	len2 = new Length(1.0,LengthUnit.INCHES);
    	assertEquals(5.08,len1.add(len2).getValue(),0.0001);
    }
    
    @Test
    public void testAdditionCommutativity() throws InvalidUnitMeasurementException{
    	len1 = new Length(2.54,LengthUnit.CENTIMETRE);
    	len2 = new Length(1.0,LengthUnit.INCHES);
    	Length l1 = len1.add(len2);
    	Length l2 = len2.add(len1);
    	l2 = l2.convertTo(l1.getLen());
    	assertTrue(l1.equals(l2));
    }
    
    @Test
    public void testadditionWithZero() throws InvalidUnitMeasurementException {
    	len1 = new Length(5.0,LengthUnit.FEET);
    	len2 = new Length(0.0,LengthUnit.INCHES);
    	assertEquals(5.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionNegativeValues() throws InvalidUnitMeasurementException{
    	len1 = new Length(5.0,LengthUnit.FEET);
    	len2 = new Length(-3.0,LengthUnit.FEET);
    	assertEquals(2.0,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionNullSecondOperand() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.FEET);
    	assertThrows(IllegalArgumentException.class,()->{
    		QuantityMeasurementApp.demonstrateLengthAddition(len1,null);
    	});
    }
    
    @Test
    public void testAdditionLargeValues() throws InvalidUnitMeasurementException{
    	len1 = new Length(1e-6,LengthUnit.FEET);
    	len2 = new Length(1e-6,LengthUnit.FEET);
    	assertEquals(2e-6,len1.add(len2).getValue());
    }
    
    @Test
    public void testAdditionSmallValues() throws InvalidUnitMeasurementException{
    	len1 = new Length(0.001,LengthUnit.FEET);
    	len2 = new Length(0.002,LengthUnit.FEET);
    	assertEquals(0.003,len1.add(len2).getValue(),0.0001);
    }
    
//    Addition of two unit two specific unit 
    
    @Test
    public void testAdditionExplicitTargetUnitFeet() throws InvalidUnitMeasurementException {
    	len1 = new Length(1.0,LengthUnit.FEET);
    	len2 = new Length(12.0,LengthUnit.INCHES);
    	assertEquals(2.0,len1.add(len2,LengthUnit.FEET).getValue());
    }
    
    @Test
    public void testAdditionExplicitTargetUnitInches() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.FEET);
    	len2 = new Length(12.0,LengthUnit.INCHES);
    	assertEquals(24.0,len1.add(len2,LengthUnit.INCHES).getValue());
    }
    
    @Test
    public void testAdditionExplicitTargetUnitYards() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.FEET);
    	len2 = new Length(12.0,LengthUnit.INCHES);
    	assertEquals(0.667,len1.add(len2,LengthUnit.YARD).getValue(),0.001);
    }
    
    @Test
    public void testAdditionExplicitTargetUnitCentimeter() throws InvalidUnitMeasurementException{
    	len1 = new Length(1.0,LengthUnit.INCHES);
    	len2 = new Length(1.0,LengthUnit.INCHES);
    	assertEquals(5.08,len1.add(len2,LengthUnit.CENTIMETRE).getValue(),0.001);
    }
     @Test
     public void testAdditionExplicitTargetUnitSameAsFirstOperand() throws InvalidUnitMeasurementException {
    	 len1 = new Length(2.0,LengthUnit.YARD);
     	len2 = new Length(3.0,LengthUnit.FEET);
     	assertEquals(3.0,len1.add(len2,LengthUnit.YARD).getValue());
     }
     
     @Test
     public void testAdditionExplicitTargetUnitSameAsSecondOprand() throws InvalidUnitMeasurementException{
    	 len1 = new Length(2.0,LengthUnit.YARD);
      	len2 = new Length(3.0,LengthUnit.FEET);
      	assertEquals(9.0,len1.add(len2,LengthUnit.FEET).getValue());
     }
     
     @Test
     public void testAdditionTargetUnitCommutativity() throws InvalidUnitMeasurementException{
    	 len1 = new Length(1.0,LengthUnit.FEET);
      	len2 = new Length(12.0,LengthUnit.INCHES);
      	Length temp1 = len1.add(len2,LengthUnit.YARD);
        len1 = new Length(12.0,LengthUnit.INCHES);
     	len2 = new Length(1.0,LengthUnit.FEET);
     	Length temp2 = len1.add(len2,LengthUnit.YARD);
     	assertTrue(temp1.equals(temp2));
     	
     }
     
     @Test
     public void testAdditionTargetUnitWithZero() throws InvalidUnitMeasurementException{
    	 len1 = new Length(5.0,LengthUnit.FEET);
      	len2 = new Length(0.0,LengthUnit.INCHES);
      	assertEquals(1.667,len1.add(len2,LengthUnit.YARD).getValue(),0.001);
     }
     
     @Test
     public void testAdditionTargetUnitNagativeValues() throws InvalidUnitMeasurementException{
    	 len1 = new Length(5.0,LengthUnit.FEET);
      	len2 = new Length(-2.0,LengthUnit.FEET);
      	assertEquals(36.0,len1.add(len2,LengthUnit.INCHES).getValue());
     }
     
     @Test
     public void testAdditionTargetUnitNullTargetUnit() throws InvalidUnitMeasurementException{
    	 len1 = new Length(2.0,LengthUnit.YARD);
      	len2 = new Length(3.0,LengthUnit.FEET);
      	assertThrows(IllegalArgumentException.class,()->{
      		len1.add(len2,null);
      	});
     }
     
     @Test
     public void testAdditionTargetUnitLargeToSmall() throws InvalidUnitMeasurementException{
    	 len1 = new Length(1000.0,LengthUnit.FEET);
      	len2 = new Length(500.0,LengthUnit.FEET);
      	assertEquals(18000.0,len1.add(len2,LengthUnit.INCHES).getValue());
     }
     
     @Test
     public void testAdditionTargetSmallToLarge() throws InvalidUnitMeasurementException{
    	 len1 = new Length(12.0,LengthUnit.INCHES);
      	len2 = new Length(12.0,LengthUnit.INCHES);
      	assertEquals(0.667,len1.add(len2,LengthUnit.YARD).getValue(),0.001);
     }
     @Test
     public void testAddition_ExplicitTargetUnit_PrecisionTolerance() throws InvalidUnitMeasurementException {
         Length l1 = new Length(1.0, LengthUnit.FEET);
         Length l2 = new Length(0.1, LengthUnit.FEET);
         Length result = l1.add(l2, LengthUnit.INCHES);
         assertEquals(13.2, result.getValue(),0.001);
     } 
    
} 