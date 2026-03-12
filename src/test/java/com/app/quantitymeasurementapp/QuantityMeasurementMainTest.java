
package com.app.quantitymeasurementapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.apps.quantitymeasurementapp.exception.InvalidUnitMeasurementException;
import com.apps.quantitymeasurementapp.quantity.Quantity;
import com.apps.quantitymeasurementapp.unit.IMeasurable;
import com.apps.quantitymeasurementapp.unit.LengthUnit;
import com.apps.quantitymeasurementapp.unit.QuantityMeasurementApp;
import com.apps.quantitymeasurementapp.unit.TemperatureUnit;
import com.apps.quantitymeasurementapp.unit.VolumeUnit;
import com.apps.quantitymeasurementapp.unit.WeightUnit;

class QuantityMeasurementMainTest<U extends IMeasurable> {
	
	Quantity<LengthUnit> len1;
	Quantity<LengthUnit> len2;
	
	Quantity<WeightUnit> w1;
	Quantity<WeightUnit> w2;
	
	
	Quantity<VolumeUnit> v1;
	Quantity<VolumeUnit> v2;
	
	Quantity<TemperatureUnit> t1;
	Quantity<TemperatureUnit> t2;
	
	
	
	@Test
    public void testEquality_SameUnitAndValue_ShouldReturnTrue() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> feet1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> feet2 = new Quantity<>(10.0, LengthUnit.FEET);
        
        // Using the new Generic App Logic
        assertTrue(QuantityMeasurementApp.demonstrateEquality(feet1, feet2));
    }

    @Test
    public void testEquality_DifferentUnitsSameValue_ShouldReturnTrue() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> oneFeet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches = new Quantity<>(12.0, LengthUnit.INCHES);
        
        assertTrue(QuantityMeasurementApp.demonstrateEquality(oneFeet, twelveInches));
    }

    @Test
    public void testEquality_DifferentValues_ShouldReturnFalse() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> feet1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> feet2 = new Quantity<>(1.1, LengthUnit.FEET);
        
        assertFalse(QuantityMeasurementApp.demonstrateEquality(feet1, feet2));
    }

    @Test
    public void testEquality_NullComparison_ShouldThrowException() {
        Quantity<LengthUnit> feet1 = new Quantity<>(1.0, LengthUnit.FEET);
        
        assertThrows(InvalidUnitMeasurementException.class, () -> {
            QuantityMeasurementApp.demonstrateEquality(feet1, null);
        });
    }

    @Test
    public void testEquality_SameReference_ShouldReturnTrue() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> feet1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> feet2 = feet1;
        
        assertTrue(QuantityMeasurementApp.demonstrateEquality(feet1, feet2));
    }

    // --- Conversion Tests ---

    @Test
    public void testConversion_YardToInches() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> yard = new Quantity<>(1.0, LengthUnit.YARD);
        Quantity<LengthUnit> result = QuantityMeasurementApp.demonstrateConversion(yard, LengthUnit.INCHES);
        
        assertEquals(36.0, result.getValue(), 0.01);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    public void testConversion_FeetToCm() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> foot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = QuantityMeasurementApp.demonstrateConversion(foot, LengthUnit.CENTIMETERS);
        
        assertEquals(30.48, result.getValue(), 0.01);
    }

    // --- Addition Tests ---

    @Test
    public void testAddition_TwoDifferentUnits() throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET); // 12 inches
        Quantity<LengthUnit> inches = new Quantity<>(2.0, LengthUnit.INCHES);
        
        // Assuming demonstrateAddition returns sum in a default or base unit
        Quantity<LengthUnit> sum = QuantityMeasurementApp.demonstrateAddition(inches, feet);
        Quantity<LengthUnit> target = new Quantity<>(14.0, LengthUnit.INCHES);
        // If the sum converts both to inches: 12 + 2 = 14
        assertTrue(sum.equals(target));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.0, 5.0})
    public void testMultipleFeetToInchesEquality(double value) throws InvalidUnitMeasurementException {
        Quantity<LengthUnit> feet = new Quantity<>(value, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(value * 12, LengthUnit.INCHES);
        
        assertTrue(QuantityMeasurementApp.demonstrateEquality(feet, inches));
    }
	
	// Yard

	@Test
	public void yardEquals36Inches() {
	    assertTrue(new Quantity<LengthUnit>(1.0, LengthUnit.YARD)
	            .equals(new Quantity<LengthUnit>(36.0, LengthUnit.INCHES)));
	}

	@Test
	public void centimeterEquals39Point3701Inches() {
	    assertTrue(new Quantity<LengthUnit>(100.0, LengthUnit.CENTIMETERS)
	            .equals(new Quantity<LengthUnit>(39.37, LengthUnit.INCHES)));
	}

	@Test
	public void threeFeetEqualsOneYard() {
	    assertTrue(new Quantity<LengthUnit>(3.0, LengthUnit.FEET)
	            .equals(new Quantity<LengthUnit>(1.0, LengthUnit.YARD)));        
	}

	@Test
	public void thirtyPoint48CmEqualsOneFoot() {
	    assertTrue(new Quantity<LengthUnit>(30.48, LengthUnit.CENTIMETERS)
	            .equals(new Quantity<LengthUnit>(1.0, LengthUnit.FEET)));
	}

	@Test
	public void yardNotEqualToInches() {
	    assertFalse(new Quantity<LengthUnit>(100.0, LengthUnit.YARD)
	            .equals(new Quantity<LengthUnit>(39.37, LengthUnit.INCHES)));
	}

	@Test
	public void referenceEqualitySameObject() {
	    len1 = new Quantity<LengthUnit>(1.0, LengthUnit.YARD);
	    len2 = len1;
	    assertTrue(len1.equals(len1));
	}

	@Test
	public void equalsReturnsFalseForNull() {
	    len1 = new Quantity<LengthUnit>(1.0, LengthUnit.YARD);
	    assertFalse(len1.equals(null));
	}

	@Test
	public void reflexiveSymmetricAndTransitiveProperty() {
		Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARD);
		Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
		Quantity<LengthUnit> c = new Quantity<>(36.0, LengthUnit.INCHES);

	    // Reflexive
	    assertTrue(a.equals(a));

	    // Symmetric
	    assertTrue(a.equals(b));
	    assertTrue(b.equals(a));

	    // Transitive
	    assertTrue(a.equals(b));
	    assertTrue(b.equals(c));
	    assertTrue(a.equals(c));
	}

	@Test
	public void differentValuesSameUnitNotEqual() {
	    assertFalse(new Quantity<LengthUnit>(10.5, LengthUnit.FEET)
	            .equals(new Quantity<LengthUnit>(123.2, LengthUnit.FEET)));
	}

	@Test
	public void crossUnitEqualityDemonstrateMethod() {
		Quantity<LengthUnit> yard = new Quantity<LengthUnit>(1.0, LengthUnit.YARD);
		Quantity<LengthUnit> inch = new Quantity<LengthUnit>(36.0, LengthUnit.INCHES);

	    assertTrue(yard.equals(inch));
	}

	@Test
	public void convertFeetToInches() throws InvalidUnitMeasurementException {
		Quantity<LengthUnit> lengthInches = QuantityMeasurementApp
	            .demonstrateConversion(new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.INCHES);
		Quantity<LengthUnit> expectedInches = new Quantity<LengthUnit>(36.0, LengthUnit.INCHES);
	    assertTrue(QuantityMeasurementApp
	            .demonstrateEquality(lengthInches, expectedInches));
	}

	@Test
	public void convertYardToInchesUsingOverloadMethod() throws InvalidUnitMeasurementException {
		Quantity<LengthUnit> lengthInYard = new Quantity<LengthUnit>(2.0, LengthUnit.YARD);
		Quantity<LengthUnit> lengthInInches = QuantityMeasurementApp
	            .demonstrateConversion(lengthInYard, LengthUnit.INCHES);
		Quantity<LengthUnit> expected = new Quantity<LengthUnit>(72.0, LengthUnit.INCHES);
	    assertTrue(QuantityMeasurementApp
	            .demonstrateEquality(lengthInInches, expected));
	}

	@Test
	public void addFeetAndInches() throws InvalidUnitMeasurementException {
		Quantity<LengthUnit> Feet = new Quantity<>(1.0, LengthUnit.FEET);
	    Quantity<LengthUnit> Inches = new Quantity<>(24.0, LengthUnit.INCHES);
	    Quantity<LengthUnit> output = QuantityMeasurementApp.demonstrateAddition(Feet, Inches);
	    Quantity<LengthUnit> result = new Quantity<>(3.0, LengthUnit.FEET);
	    assertTrue(output.equals(result));
	}



	@Test
	void addFeetAndInchesWithTargetUnitInches() throws InvalidUnitMeasurementException {
		Quantity<LengthUnit> Feet = new Quantity<>(1.0, LengthUnit.FEET);
	    Quantity<LengthUnit> Inches = new Quantity<>(24.0, LengthUnit.INCHES);
	    Quantity<LengthUnit> output = QuantityMeasurementApp
	            .demonstrateAddition(Feet, Inches, LengthUnit.INCHES);
	    Quantity<LengthUnit> result = new Quantity<>(3.0, LengthUnit.FEET);
	    assertTrue(output.equals(result));
	}


	//Verifies that Quantity(1.0, KILOGRAM).equals(Quantity(1.0, KILOGRAM)) returns true.
	//Tests: equals() returns true for identical kilogram measurements.
	@Test
	public void testEquality_KilogramToKilogram_SameValue(){
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		assertTrue(w1.equals(w2));
	}
	

	//Verifies that Quantity(1.0, KILOGRAM).equals(Quantity(2.0, KILOGRAM)) returns false.
	//Tests: equals() returns false for different kilogram measurements.
	@Test
	public void testEquality_KilogramToKilogram_DifferentValue() {
		w1 = new Quantity<>(12.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		assertFalse(w1.equals(w2));
	}


	//Verifies that Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM)) returns true.
	//Tests: equals() returns true for kilogram-to-gram conversion.
	@Test
	public void testEquality_KilogramToGram_EquivalentValue(){
		w1 = new Quantity<>(12.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(12000.0,WeightUnit.GRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(1000.0, GRAM).equals(Quantity(1.0, KILOGRAM)) returns true.
	//Tests: equals() returns true (tests symmetry of conversion).
	@Test
	public void testEquality_GramToKilogram_EquivalentValue() {
		w1 = new Quantity<>(12000.0,WeightUnit.GRAM);
		w2 = new Quantity<>(12.0,WeightUnit.KILOGRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FOOT)) returns false.
	//Tests: equals() returns false when comparing incompatible measurement categories.
	@Test
	public void testEquality_WeightVsLength_Incompatible() {
		w1 = new Quantity<>(10.0,WeightUnit.KILOGRAM);
		len1 = new Quantity<>(1.0,LengthUnit.FEET);
		assertFalse(w1.equals(w2));
	}

	//Verifies that Quantity(1.0, KILOGRAM).equals(null) returns false.
	//Tests: equals() returns false when comparing with null.
	@Test
	public void testEquality_NullComparison() {
		w1 = new Quantity<>(10.0,WeightUnit.KILOGRAM);
		assertFalse(w1.equals(null));
	}

	//Verifies that a weight object equals itself (reflexive property).
	//Tests: equals() returns true when comparing an object with itself.
	@Test
	public void testEquality_SameReference() {
		w1 = new Quantity<>(12000.0,WeightUnit.GRAM);
		w2 = w1;
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(1.0, null) throws IllegalArgumentException.
	//Tests: Exception thrown for null unit in constructor.
	@Test
	public void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class,()-> {
			w1 = new Quantity<>(12000.0,null);
		});
	}

	//Verifies transitive property: if A equals B and B equals C, then A equals C.
	//Example: Quantity(1.0, KILOGRAM) equals Quantity(1000.0, GRAM) and Quantity(1000.0, GRAM) equals Quantity(1.0, KILOGRAM), therefore Quantity(1.0, KILOGRAM) equals Quantity(1.0, KILOGRAM).
	@Test
	public void testEquality_TransitiveProperty() {
		w1 = new Quantity<>(12000.0,WeightUnit.GRAM);
		w2 = new Quantity<>(12.0,WeightUnit.KILOGRAM);
		assertTrue((w1.equals(w2)) && (w2.equals(w1)));
	}
	

	//Verifies that Quantity(0.0, KILOGRAM).equals(Quantity(0.0, GRAM)) returns true.
	//Tests: Zero values are considered equal across units.
	@Test
	public void testEquality_ZeroValue() {
		w1 = new Quantity<>(0.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(0.0,WeightUnit.GRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(-1.0, KILOGRAM).equals(Quantity(-1000.0, GRAM)) returns true.
	//Tests: Negative weight values are handled correctly in conversions.
	@Test
	public void testEquality_NegativeWeight() {
		w1 = new Quantity<>(-1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(-1000.0,WeightUnit.GRAM);
		assertTrue(w1.equals(w2));
	}

	
	//Verifies that Quantity(1000000.0, GRAM).equals(Quantity(1000.0, KILOGRAM)) returns true.
	//Tests: Large magnitude values maintain precision across conversions.
	@Test
	public void testEquality_LargeWeightValue() {
		w1 = new Quantity<>(1000000.0,WeightUnit.GRAM);
		w2 = new Quantity<>(1000.0,WeightUnit.KILOGRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(0.001, KILOGRAM).equals(Quantity(1.0, GRAM)) returns true.
	//Tests: Small magnitude values maintain precision across conversions.
	@Test
	public void testEquality_SmallWeightValue() {
		w1 = new Quantity<>(0.001,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1.0,WeightUnit.GRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(2.20462, POUND).convertTo(KILOGRAM) returns Quantity(~1.0, KILOGRAM) (within epsilon).
	//Tests: Conversion from pound to kilogram.
	@Test
	public void testConversion_PoundToKilogram() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(2.20462,WeightUnit.POUND);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> result = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(1.0, KILOGRAM).convertTo(POUND) returns Quantity(~2.20462, POUND) (within epsilon).
	//Tests: Conversion from kilogram to pound.
	@Test
	public void testConversion_KilogramToPound() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.POUND);
		Quantity<WeightUnit> result = new Quantity<>(2.20,WeightUnit.POUND);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(5.0, KILOGRAM).convertTo(KILOGRAM) returns Quantity(5.0, KILOGRAM).
	//Tests: Converting to the same unit returns unchanged value.
	@Test
	public void testConversion_SameUnit() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(5.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> result = new Quantity<>(5.0,WeightUnit.KILOGRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(0.0, KILOGRAM).convertTo(GRAM) returns Quantity(0.0, GRAM).
	//Tests: Zero value conversion across units.
	@Test
	public void testConversion_ZeroValue() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(0.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.GRAM);
		Quantity<WeightUnit> result = new Quantity<>(0.0,WeightUnit.GRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(-1.0, KILOGRAM).convertTo(GRAM) returns Quantity(-1000.0, GRAM).
	//Tests: Negative weight conversion preserves sign.
	@Test
	public void testConversion_NegativeValue() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(-1.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.GRAM);
		Quantity<WeightUnit> result = new Quantity<>(-1000.0,WeightUnit.GRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity (1.5, KILOGRAM).convertTo(GRAM) .convertTo(KILOGRAM) returns Quantity(~1.5, KILOGRAM) (within epsilon).
	//Tests: Round-trip conversions preserve value within floating-point tolerance.
	@Test
	public void testConversion_RoundTrip() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.5,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateConversion(w1, WeightUnit.GRAM);
		Quantity<WeightUnit> result = new Quantity<>(1.5,WeightUnit.KILOGRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(1.0, KILOGRAM).add(Quantity(2.0, KILOGRAM)) returns Quantity(3.0, KILOGRAM).
	//Tests: Same-unit addition without conversion.
	@Test
	public void testAddition_SameUnit_KilogramPlusKilogram() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(2.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(3.0,WeightUnit.KILOGRAM);
		assertTrue(output.equals(result));
	} 

	//Verifies that Quantity(1.0, KILOGRAM).equals(Quantity(1.0, KILOGRAM)) returns true.
	//Tests: equals() returns true for identical kilogram measurements.
	@Test
	public void testAddition_CrossUnit_KilogramPlusGram() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		assertTrue(w1.equals(w2));
	}

	//Verifies that Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM)) returns Quantity(2.0, KILOGRAM).
	//Tests: Cross-unit addition will result in the first operand's unit.
	@Test
	public void testLengthUnitEnum_InchesConstant() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1000.0,WeightUnit.GRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(2.0,WeightUnit.KILOGRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(2.20462, POUND).add(Quantity(1.0, KILOGRAM)) returns Quantity(~4.40924, POUND).
	//Tests: Cross-unit addition with mixed metric and imperial units.
	@Test
	public void testAddition_CrossUnit_PoundPlusKilogram() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(2.20462,WeightUnit.POUND);
		w2 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(4.41,WeightUnit.POUND);
		assertTrue(output.equals(result));
	} 

	//Verifies that Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM) returns Quantity(2000.0, GRAM).
	//Tests: Explicit target unit specification in gram.
	@Test
	public void testAddition_ExplicitTargetUnit_Kilogram() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1000.0,WeightUnit.GRAM);
		Quantity<WeightUnit> output = QuantityMeasurementApp.demonstrateAddition(w1, w2, WeightUnit.GRAM);
		Quantity<WeightUnit> result = new Quantity<>(2000.0,WeightUnit.GRAM);
		assertTrue(output.equals(result));
	}

	//Verifies that Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM)) equals Quantity(1000.0, GRAM).add(Quantity(1.0, KILOGRAM)) in their respective units.
	//Tests: Addition is commutative with appropriate unit conversions.
	@Test
	public void testAddition_Commutativity() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1000.0,WeightUnit.GRAM);
		Quantity<WeightUnit> output1 = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> output2 = QuantityMeasurementApp.demonstrateAddition(w2, w1);
		assertTrue(output1.equals(output2));
	}

	//Verifies that Quantity(5.0, KILOGRAM).add(Quantity(0.0, GRAM)) returns Quantity(5.0, KILOGRAM).
	//Tests: Adding zero acts as an identity element.
	@Test
	public void testAddition_WithZero() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(5.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(0.0,WeightUnit.GRAM);
		Quantity<WeightUnit> output1 = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(5.0,WeightUnit.KILOGRAM);
		assertTrue(output1.equals(result));
	}

	//Verifies that Quantity(5.0, KILOGRAM).add(Quantity(-2000.0, GRAM)) returns Quantity(3.0, KILOGRAM).
	//Tests: Addition with negative measurements.
	@Test
	public void testAddition_NegativeValues() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(5.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(-2000.0,WeightUnit.GRAM);
		Quantity<WeightUnit> output1 = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(3.0,WeightUnit.KILOGRAM);
		assertTrue(output1.equals(result));
	}

	//Verifies that Quantity(1e6, KILOGRAM).add(Quantity(1e6, KILOGRAM)) returns Quantity(2e6, KILOGRAM).
	//Tests: Addition with large magnitude values.
	@Test
	public void testAddition_LargeValues() throws InvalidUnitMeasurementException {
		w1 = new Quantity<>(1e6,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1e6,WeightUnit.KILOGRAM);
		Quantity<WeightUnit> output1 = QuantityMeasurementApp.demonstrateAddition(w1, w2);
		Quantity<WeightUnit> result = new Quantity<>(2e6,WeightUnit.KILOGRAM);
		assertTrue(output1.equals(result));
	}
	
	//UC-11
	
	@Test
	public void testEquality_LitreToLitre_SameValue() {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}

	@Test
	public void testEquality_LitreToLitre_DifferentValue() {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(2.0, VolumeUnit.LITRE);
		assertFalse(v1.equals(v2));
	}

	
	@Test
	public void testEquality_LitreToMillilitre_EquivalentValue() {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		assertTrue(v1.equals(v2));
	}

	
	@Test
	public void testEquality_MillilitreToLitre_EquivalentValue() {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));	
	}

	@Test
	public void testEquality_LitreToGallon_EquivalentValue() {
		v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		v2 = new Quantity<>(1.0, VolumeUnit.GALLON);
		assertTrue(v1.equals(v2));
	}

	
	@Test
	public void testEquality_GallonToLitre_EquivalentValue() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		v2 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}
	

	@Test
	public void testEquality_VolumeVsLength_Incompatible() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		len2 = new Quantity<>(3.78541, LengthUnit.FEET);
		assertFalse(v1.equals(v2));
	}
	

	@Test
	public void testEquality_VolumeVsWeight_Incompatible() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		w2 = new Quantity<>(3.78541, WeightUnit.KILOGRAM);
		assertFalse(v1.equals(v2));
	}
	

	@Test
	public void testEquality_NullComparisons() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		assertFalse(v1.equals(null));
	}
	

	@Test
	public void testEquality_SameReferences() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		v2 = v1;
		assertTrue(v1.equals(v2));
	}
	

	@Test
	public void testEquality_NullUnits() {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		assertFalse(v1.equals(null));
	}
	
	@Test
	public void testEquality_TransitivePropertys() {
		Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> c = new Quantity<>(0.264172, VolumeUnit.GALLON);

	    // Reflexive
	    assertTrue(a.equals(a));

	    // Symmetric
	    assertTrue(a.equals(b));
	    assertTrue(b.equals(a));

	    // Transitive
	    assertTrue(a.equals(b));
	    assertTrue(b.equals(c));
	    assertTrue(a.equals(c));
	}
	

	@Test
	public void testEquality_ZeroValues() {
		v1 = new Quantity<>(0.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(0.0, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}

	@Test
	public void testEquality_NegativeVolume() {
		v1 = new Quantity<>(-1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(-1.0, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}
	
	@Test
	public void testEquality_LargeVolumeValue() {
		v1 = new Quantity<>(1e6, VolumeUnit.LITRE);
		v2 = new Quantity<>(1e6, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}

	@Test
	public void testEquality_SmallVolumeValue() {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertTrue(v1.equals(v2));
	}

	@Test
	public void testConversion_LitreToMillilitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConversion_MillilitreToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testConversion_GallonToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(3.79, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}
	

	@Test
	public void testConversion_LitreToGallon() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.GALLON);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConversion_MillilitreToGallon() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected = new Quantity<>(0.26, VolumeUnit.GALLON);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testAddition_SameUnit_LitrePlusLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2);
		Quantity<VolumeUnit> expected = new Quantity<>(2.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testAddition_SameUnit_MillilitrePlusMillilitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2);
		Quantity<VolumeUnit> expected = new Quantity<>(2.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testAddition_CrossUnit_LitrePlusMillilitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		v2 = new Quantity<>(4000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2);
		Quantity<VolumeUnit> expected = new Quantity<>(5.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testAddition_CrossUnit_MillilitrePlusLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(3000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(2.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2);
		Quantity<VolumeUnit> expected = new Quantity<>(5.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testAddition_CrossUnit_GallonPlusLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		v2 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2);
		Quantity<VolumeUnit> expected = new Quantity<>(1.26, VolumeUnit.GALLON);	
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testAddition_ExplicitTargetUnit_Litre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(2000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(3.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(5.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Millilitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(2000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(3.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(5000.0, VolumeUnit.MILLILITRE);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testAddition_ExplicitTargetUnit_Gallon() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(2.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.GALLON);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testAddition_Commutativitys() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(2.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result2 = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.GALLON);
		Quantity<VolumeUnit> result1 = QuantityMeasurementApp.demonstrateAddition(v2, v1, VolumeUnit.GALLON);
		assertTrue(result1.equals(result2));
	}

	@Test
	public void testVolumeUnitEnum_LitreConstant() {
		double result = VolumeUnit.LITRE.convertToBaseUnit(1.0);
	    assertEquals(1.0, result, 0.0001);
	}
	

	@Test
	public void testVolumeUnitEnum_MillilitreConstant() {
		double result = VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0);
	    assertEquals(1.0, result, 0.0001);
	}

	@Test
	public void testVolumeUnitEnum_GallonConstant() {
		double result = VolumeUnit.GALLON.convertToBaseUnit(1.0);
	    assertEquals(3.78541, result, 0.0001);
	}

	@Test
	public void testConvertToBaseUnit_LitreToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}	

	@Test
	public void testConvertToBaseUnit_MillilitreToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(5000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(5.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConvertToBaseUnit_GallonToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(3.79, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConvertFromBaseUnit_LitreToLitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.LITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConvertFromBaseUnit_LitreToMillilitre() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);		
		assertTrue(result.equals(expected));
	}

	@Test
	public void testConvertFromBaseUnit_LitreToGallon() throws InvalidUnitMeasurementException {
		v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected = new Quantity<>(1.0, VolumeUnit.GALLON);		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void testBackwardCompatibility_AllUC1Through10Tests() throws InvalidUnitMeasurementException {
		Quantity<LengthUnit> feet1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> feet2 = new Quantity<>(10.0, LengthUnit.FEET);
        
        // Using the new Generic App Logic
        assertTrue(QuantityMeasurementApp.demonstrateEquality(feet1, feet2));
        
        w1 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		w2 = new Quantity<>(1.0,WeightUnit.KILOGRAM);
		assertTrue(w1.equals(w2));
	}
	
	@Test
	public void testGenericQuantity_VolumeOperations_Consistency() throws InvalidUnitMeasurementException {
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(v1.equals(v2));
        
        v1 = new Quantity<>(2000.0, VolumeUnit.MILLILITRE);
		v2 = new Quantity<>(3.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(5000.0, VolumeUnit.MILLILITRE);		
		assertTrue(result.equals(expected));
        
        
        v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result1 = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected1 = new Quantity<>(1.0, VolumeUnit.GALLON);		
		assertTrue(result1.equals(expected1));
        
	}

	@Test
	public void testScalability_VolumeIntegration() throws InvalidUnitMeasurementException {
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> v2 = new Quantity<>(3.78541, VolumeUnit.LITRE);
        assertTrue(v1.equals(v2));
        
        v1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result1 = QuantityMeasurementApp.demonstrateConversion(v1, VolumeUnit.GALLON);
		Quantity<VolumeUnit> expected1 = new Quantity<>(1.0, VolumeUnit.GALLON);		
		assertTrue(result1.equals(expected1));
        
        
        v1 = new Quantity<>(1.0, VolumeUnit.GALLON);
        v2 = new Quantity<>(3.78541, VolumeUnit.LITRE);
		Quantity<VolumeUnit> result = QuantityMeasurementApp.demonstrateAddition(v1, v2, VolumeUnit.LITRE);
		Quantity<VolumeUnit> expected = new Quantity<>(7.57, VolumeUnit.LITRE);		
		assertTrue(expected.equals(result));
	}
	
	//UC-12
	
	 @Test
    public void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.subtract(q2);

        assertTrue(result.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }
	 
	@Test
	public void testSubtraction_SameUnit_LitreMinusLitre() {
		Quantity<VolumeUnit> q1 = new Quantity<>(12.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(4.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> result = q1.subtract(q2);

        assertTrue(result.equals(new Quantity<>(8.0, VolumeUnit.LITRE)));
	}
	
	@Test
    public void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);

        assertTrue(q1.subtract(q2).equals(
                new Quantity<>(9.5, LengthUnit.FEET)));
    }

    @Test
    public void testSubtraction_CrossUnit_InchesMinusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(120.0, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        assertTrue(q1.subtract(q2).equals(
                new Quantity<>(60.0, LengthUnit.INCHES)));
    }

    @Test
    public void testSubtraction_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                q1.subtract(q2, LengthUnit.INCHES);

        assertTrue(result.equals(
                new Quantity<>(114.0, LengthUnit.INCHES)));
    }

    @Test
    public void testSubtraction_ResultingInNegative() {
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);

        assertTrue(q1.subtract(q2).equals(
                new Quantity<>(-5.0, LengthUnit.FEET)));
    }

    @Test
    public void testSubtraction_ResultingInZero() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(120.0, LengthUnit.INCHES);

        assertTrue(q1.subtract(q2).equals(
                new Quantity<>(0.0, LengthUnit.FEET)));
    }

    public void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class,()->{
        	Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
            q1.subtract(null);
        });
    }

    @Test
    public void testSubtraction_Immutability() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        q1.subtract(q2);

        assertTrue(q1.equals(new Quantity<>(10.0, LengthUnit.FEET)));
        assertTrue(q2.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }
	
    @Test
    public void testDivision_SameUnit_FeetDividedByFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        
        Quantity<LengthUnit> result = new Quantity<LengthUnit>( QuantityMeasurementApp.demonstrateDivision(q1, q2), LengthUnit.FEET);
        Quantity<LengthUnit> expected = new Quantity<>(5.0, LengthUnit.FEET);;
        assertTrue(result.equals(expected));
    }

    @Test
    public void testDivision_CrossUnit_FeetDividedByInches() {
        Quantity<LengthUnit> q1 = new Quantity<>(24.0, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = new Quantity<LengthUnit>(QuantityMeasurementApp.demonstrateDivision(q1, q2), LengthUnit.INCHES);
        Quantity<LengthUnit> expected = new Quantity<>(12.0, LengthUnit.INCHES);;
        assertTrue(result.equals(expected));
    }
    
    public void testDivision_ByZero() {
    	assertThrows(ArithmeticException.class,()->{
		 Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
	     Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.FEET);

	        q1.divide(q2);
    	});
    }

    public void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class,()->{
        	Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
            q1.divide(null);
        });
    }

    @Test
    public void testDivision_Immutability() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        q1.divide(q2);

        assertTrue(q1.equals(new Quantity<>(10.0, LengthUnit.FEET)));
        assertTrue(q2.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }
    
    //uc14
    
    @Test
    public void testTemperatureEquality_CelsiusToCelsius_SameValue() {
	    	t1 = new Quantity<TemperatureUnit>(10.0, TemperatureUnit.CELSIUS);
	    	t2 = new Quantity<TemperatureUnit>(10.0, TemperatureUnit.CELSIUS);
	    	assertTrue(t1.equals(t2));
    }
    
    @Test
    public void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
	    	t1 = new Quantity<TemperatureUnit>(10.0, TemperatureUnit.FAHRENHEIT);
	    	t2 = new Quantity<TemperatureUnit>(10.0, TemperatureUnit.FAHRENHEIT);
	    	assertTrue(t1.equals(t2));
    }
    
    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
    		assertTrue(new Quantity<>(0.0,TemperatureUnit.CELSIUS).equals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
    }
    
    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit() {
    		assertTrue(new Quantity<>(100.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT)));
    }
    
    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
    		assertTrue(new Quantity<>(-40.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT)));
    }
    
    @Test
    public void testTemperatureEquality_SymmetricProperty() {
	    	t1 = new Quantity<TemperatureUnit>(37.0, TemperatureUnit.CELSIUS);
	    	t2 = new Quantity<TemperatureUnit>(98.6, TemperatureUnit.FAHRENHEIT);
	    	assertTrue(t1.equals(t2));
	    	assertTrue(t2.equals(t1));
    }
    
    @Test
    public void testTemperatureEquality_ReflexiveProperty() {
	    	t1 = new Quantity<TemperatureUnit>(37.0, TemperatureUnit.CELSIUS);
	    	assertTrue(t1.equals(t1));
    }
    
    @Test
    public void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() throws InvalidUnitMeasurementException {
	    	t1 = new Quantity<TemperatureUnit>(50.0, TemperatureUnit.CELSIUS);
	    	t2 = QuantityMeasurementApp.demonstrateConversion(t1, TemperatureUnit.FAHRENHEIT);
	    	assertTrue(t2.equals(new Quantity<TemperatureUnit>(122.0, TemperatureUnit.FAHRENHEIT)));
    }
    
    @Test
    public void testTemperatureConversion_FahrenheitToCelsius_VariousValues() throws InvalidUnitMeasurementException {
	    	t1 = new Quantity<TemperatureUnit>(50.0, TemperatureUnit.CELSIUS);
	    	t2 = QuantityMeasurementApp.demonstrateConversion(t1, TemperatureUnit.CELSIUS);
	    	assertTrue(t2.equals(new Quantity<TemperatureUnit>(50.0, TemperatureUnit.CELSIUS)));
    }
}
