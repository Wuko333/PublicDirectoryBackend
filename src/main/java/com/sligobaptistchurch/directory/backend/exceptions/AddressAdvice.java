package com.sligobaptistchurch.directory.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AddressAdvice {
	
	@ResponseBody
	@ExceptionHandler(AddressException.class)
	public final ResponseEntity<AddressNotFoundResponse> capabilityNotFoundResponseResponseEntity(AddressException ex) {
		AddressNotFoundResponse response = new AddressNotFoundResponse(ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}