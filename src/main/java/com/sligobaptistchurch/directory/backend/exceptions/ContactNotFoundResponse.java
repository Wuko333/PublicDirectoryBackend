package com.sligobaptistchurch.directory.backend.exceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactNotFoundResponse {
	public String contactNotFound;
	
	
	public ContactNotFoundResponse(String contactNotFound) {
		this.contactNotFound = contactNotFound;
	}
}
