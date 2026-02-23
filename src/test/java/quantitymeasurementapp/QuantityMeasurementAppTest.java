package quantitymeasurementapp;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
	    public void testFeetEquality() {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(2,LengthUnit.FEET);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testIncheEquality() {
	    	len1 = new Length(2,LengthUnit.INCHES);
	    	len2 = new Length(2,LengthUnit.INCHES);
	    	assertTrue(len1.equals(len2));
	    }
	    
	    @Test
	    public void testFeetIncheComparision() {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(24,LengthUnit.INCHES);
	    	assertTrue(len1.compare(len2));
	    }
	    
	    @Test
	    public void testFeetInEquality() {
	    	len1 = new Length(2,LengthUnit.FEET);
	    	len2 = new Length(24,LengthUnit.FEET);
	    	assertTrue(!len1.equals(len2));
	    }
	    
	    @Test
	    public void testIncheInEquality() {
	    	len1 = new Length(2,LengthUnit.INCHES);
	    	len2 = new Length(24,LengthUnit.INCHES);
	    	assertTrue(!len1.equals(len2));
	    }
	   
	    @ParameterizedTest
	    @ValueSource(doubles= {1,4,5,6,5})
	    public void multipleFeetcomparison(double feet) {
	    	len1 = new Length(feet,LengthUnit.FEET);
	    	len2 = new Length(feet*12,LengthUnit.INCHES);
	    	assertTrue(len1.compare(len2));
	    }
}