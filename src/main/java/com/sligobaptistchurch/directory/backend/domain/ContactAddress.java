package com.sligobaptistchurch.directory.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ContactAddress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String addressline1;
	private String addressline2;
	private String city;
	private String province;
	private String state;
	private Short zipcode;
	private String country;
	private String countryCode;
	
	@JsonBackReference
	@OneToOne
    @JoinColumn(name = "personcontact_id")
	private PersonContact personContact;

	public ContactAddress(String addressLine1, String addressLine2, String city, String province, String state,
			Short zipcode, String country, String countryCode) {
		super();
		this.addressline1 = addressLine1;
		this.addressline2 = addressLine2;
		this.city = city;
		this.province = province;
		this.state = state;
		this.zipcode = zipcode;
		this.country = country;
		this.countryCode = countryCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddressline1() {
		return addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Short getZipcode() {
		return zipcode;
	}

	public void setZipcode(Short zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public PersonContact getPersonContact() {
		return personContact;
	}

	public void setPersonContact(PersonContact personContact) {
		this.personContact = personContact;
	}
	
	
	
	
}
