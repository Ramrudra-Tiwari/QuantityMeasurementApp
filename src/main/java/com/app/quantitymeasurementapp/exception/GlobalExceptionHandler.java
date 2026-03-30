package com.app.quantitymeasurementapp.exception;
//hiii


import java.util.logging.Logger;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler. class.getName());
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

	logger.warning(String. format( "The Exception is %s", ex. getMessage()));

	List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
	List<String> errMesg = errorList.stream()
	.map(objErr -> objErr.getDefaultMessage())
	.collect(Collectors.toList());
	ErrorResponse error = new ErrorResponse();
	error.timestamp = LocalDateTime.now();
	error.status = HttpStatus.BAD_REQUEST.value();
	error.error = "Quantity Measurement Error";
	error.message = String. join("; ", errMesg);
	logger.warning("Handling QuantityMeasurementException: " + ex.getMessage() +
	" for request path: " + error.path);
	return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex,WebRequest request){

	ErrorResponse error = new ErrorResponse();
	error.timestamp = LocalDateTime.now();
	error.status = HttpStatus. INTERNAL_SERVER_ERROR. value();
	error.error = "Internal Server Error";
	error.message = ex.getMessage();
	error.path = request.getDescription( false). replace( "uri=", "");
	logger.severe("Handling global exception: " + ex.getMessage() + " for request path: " + error.path);
	return new ResponseEntity<>(error, HttpStatus. INTERNAL_SERVER_ERROR);
	}
}