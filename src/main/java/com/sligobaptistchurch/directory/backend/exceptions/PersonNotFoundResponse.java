package com.sligobaptistchurch.directory.backend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonNotFoundResponse {
	public String personNotFound;
	
	
	public PersonNotFoundResponse(String personNotFound) {
		this.personNotFound = personNotFound;
	}
}
