package com.sligobaptistchurch.directory.backend.exceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressNotFoundResponse {
	public String addressNotFound;
	
	
	public AddressNotFoundResponse(String addressNotFound) {
		this.addressNotFound = addressNotFound;
	}
}
