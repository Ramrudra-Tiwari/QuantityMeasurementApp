package com.app.quantitymeasurementapp.controller;

import com.app.quantitymeasurementapp.dto.*;
import com.app.quantitymeasurementapp.services.IQuantityMeasurementService;

import java.util.List;
import java.util.logging.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io. swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media. ExampleObject;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {
	
	  private static final Logger logger = Logger.getLogger(QuantityMeasurementController.class.getName());
	  
	  private IQuantityMeasurementService quantityMeasurementService;
	   
	 public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
		 this.quantityMeasurementService = quantityMeasurementService;
	 }
	 
	 private static final String EX_FEET_INCH = """ 
	 	   { "thisQuantityDTO": {"value":1.0,"unit":"FEET", "measurementType":"LengthUnit"},
			 "thatQuantityDTO": {"value": 12.0,"unit":"INCHES", "measurementType":"LengthUnit"} }""";
	 private static final String EX_YARD_FEET = """
			 { "thisQuantityDTO": {"value":1.0,"unit":"YARDS", "measurementType":"LengthUnit"},
			 "thatQuantityDTO": {"value":3.0,"unit":"FEET", "measurementType":"LengthUnit"} }""";
	 private static final String EX_GALLON_LITRE = """
			 { "thisQuantityDTO": {"value":1.0,"unit":"GALLON", "measurementType":"VolumeUnit"},
			 "thatQuantityDTO": {"value":3.785,"unit":"LITRE", "measurementType":"VolumeUnit"} }""";
	 private static final String EX_TEMP = """
			 { "thisQuantityDTO": {"value":212.0,"unit":"FAHRENHEIT", "measurementType":"TemperatureUnit"},
			 "thatQuantityDTO": {"value":100.0,"unit":"CELSIUS", "measurementType":"TemperatureUnit"} }""";
	 private static final String EX_WITH_TARGET = """
			 { "thisQuantityDTO": {"value":1.0, "unit":"FEET", "measurementType":"LengthUnit"},
			 "thatQuantityDTO": {"value":12.0,"unit":"INCHES", "measurementType":"LengthUnit"},
			 "targetQuantityDTO": {"value":0.0, "unit":"INCHES", "measurementType":"LengthUnit"} }""";
	 
	 @PostMapping("/compare")
	 @Operation(summary = "Compare two quantities",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet = 12 Inches", value = EX_FEET_INCH),
	 @ExampleObject(name = "Yard = 3 Feet", value = EX_YARD_FEET),
	 @ExampleObject(name = "Gallon = Litres", value = EX_GALLON_LITRE),
	 @ExampleObject(name ="212°F = 100℃", value = EX_TEMP)

	 })
	 )
	 )
	 public ResponseEntity<Boolean> performComparison(@RequestBody QuantityInputDTO inputDTO){
		   return ResponseEntity.ok(quantityMeasurementService.compare(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
	 }
	 
	 @PostMapping("/convert")
	 @Operation (summary = "Convert quantity to target unit",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet -> Inches", value = EX_FEET_INCH),
	 @ExampleObject(name = "Yard -> Feet", value = EX_YARD_FEET),
	 @ExampleObject(name = "Gallon -> Litres", value = EX_GALLON_LITRE),
	 @ExampleObject(name ="212°F -> 100℃", value = EX_TEMP)

	 })
	 ))
	 public ResponseEntity<QuantityMeasurementDTO> performConversion(@RequestBody QuantityInputDTO inputDTO){
		 return ResponseEntity.ok(quantityMeasurementService.convert(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
	 }
	  
	 @PostMapping("/add")
	 @Operation(summary = "Add two quantities",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet + Inches", value = EX_FEET_INCH),
	 @ExampleObject(name = "Yard + Feet", value = EX_YARD_FEET),
	 @ExampleObject(name = "Gallon + Litres", value = EX_GALLON_LITRE)
	 })
	 ))
	 public ResponseEntity<QuantityMeasurementDTO> performAddition(@RequestBody QuantityInputDTO inputDTO){
		 return ResponseEntity.ok(quantityMeasurementService.add(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
	 }
	
	 @PostMapping("/add-with-target-unit")
	 @Operation (summary = "Add two quantities with a target unit",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet + Inches with Target Unit", value = EX_WITH_TARGET)

	 })

	 ))
	 public ResponseEntity<QuantityMeasurementDTO> performAdditionWithTargetUnit(@RequestBody QuantityInputDTO inputDTO){
		 return ResponseEntity.ok(quantityMeasurementService.add(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO(),inputDTO.getTargetQuantityDTO()));
	 }
	 
	 
	 @PostMapping("/subtract")
	 @Operation (summary = "Subtract two quantities",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet - Inches", value = EX_FEET_INCH),
	 @ExampleObject(name = "Yard - Feet", value = EX_YARD_FEET),
	 @ExampleObject(name = "Gallon - Litres", value = EX_GALLON_LITRE)

	 })

	 )
)
	 public ResponseEntity<QuantityMeasurementDTO> performSubtraction(@RequestBody QuantityInputDTO inputDTO){
		 return ResponseEntity.ok(quantityMeasurementService.subtract(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
	 }
	 
	 
	 @PostMapping("/subtract-with-target-unit")
	 @Operation (summary = "Subtract two quantities with target unit",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet - Inches with Target Unit", value = EX_WITH_TARGET)

	 } )
))
	 public ResponseEntity<QuantityMeasurementDTO> performSubtractionWithTargetUnit(@RequestBody QuantityInputDTO inputDTO){
		 return ResponseEntity.ok(quantityMeasurementService.subtract(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO(),inputDTO.getTargetQuantityDTO()));
	 }
	 
	 @PostMapping("/divide")
	 @Operation (summary = "Divide two quantities",
	 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	 content = @Content(examples = {
	 @ExampleObject(name = "Feet / Inches", value = EX_FEET_INCH),
	 @ExampleObject(name ="Yard / Feet", value = EX_YARD_FEET),
	 @ExampleObject(name = "Gallon / Litres", value = EX_GALLON_LITRE)
	 })
	 )
)
	 public ResponseEntity<QuantityMeasurementDTO> performDivision(@Valid @RequestBody QuantityInputDTO inputDTO) {
		 return ResponseEntity.ok(quantityMeasurementService.divide(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
	 }
	 
	 @GetMapping("/history/operation/{operation}")
	 @Operation(
	 summary = "Get operation history",
	 description = "Valid operations: ARITHEMETIC, CONVERSION, COMPARISION"
)
	 public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(String operation){
		 return ResponseEntity.ok(quantityMeasurementService.getOperationHistory(operation));
	 }
	 
	 @GetMapping("/history/type/{type}")
	 @Operation(
	 summary = "Get operation history by type",
	 description = "Valid types: LengthUnit, VolumeUnit, WeightUnit, TemperatureUnit"
)
	 public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistoryByType(String type){
		 return ResponseEntity.ok(quantityMeasurementService.getMeasurementsByType(type));
	 }
	 
	 @GetMapping("/count/{operation}")
	 @Operation(
	 summary = "Get operation count",
	 description = "Valid operations: ARITHEMETIC, CONVERSION, COMPARISION"
)
	 public ResponseEntity<Long> getOperationCount(String operation){
		 return ResponseEntity.ok(quantityMeasurementService.getOperationCount(operation));
	 }
	 
	 @GetMapping("/history/errored")
	 @Operation (summary = "Get errored operations history")
	 public ResponseEntity<List<QuantityMeasurementDTO>> getErroredOperations() {
		 return ResponseEntity.ok(quantityMeasurementService.getErrorHistory());
	 }
}
