package quantitymeasurementapp   ;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import quantitymeasurementapp.InvalidUnitMeasurementException;
import quantitymeasurementapp.Length;
import quantitymeasurementapp.Length.LengthUnit;
import quantitymeasurementapp.QuantityMeasurementApp;
import quantitymeasurementapp.QuantityMeasurementApp.FeetEquality;
import quantitymeasurementapp.QuantityMeasurementApp.Inches;

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
	    
}