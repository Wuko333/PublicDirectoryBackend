package com.sligobaptistchurch.directory.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class PersonAdvice {
	
	@ResponseBody
	@ExceptionHandler(PersonException.class)
	public final ResponseEntity<PersonNotFoundResponse> capabilityNotFoundResponseResponseEntity(PersonException ex) {
		PersonNotFoundResponse response = new PersonNotFoundResponse(ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
