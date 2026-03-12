
package com.app.quantitymeasurementapp;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.apps.quantitymeasurementapp.entity.QuantityDTO;
import com.apps.quantitymeasurementapp.unit.QuantityMeasurementApp;

public class QuantityMeasurementAppTest {
	private final QuantityMeasurementController controller = QuantityMeasurementApp.getInstance().controller;
	
	//here start 
	@Test
    public void testQuantityEntity_SingleOperandConstruction() {
        // Verifies correct storage using the Enum-based constructor
        QuantityDTO quantity = new QuantityDTO(1.0, "FEET", "LengthUnit");
        
        assertEquals(1.0, quantity.getValue());
        assertEquals("FEET", quantity.getUnit());
        assertEquals("LENGTH", quantity.getMeasurementType());
    }
}
