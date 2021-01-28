package com.sligobaptistchurch.directory.backend.domain;


import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;

import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PersonContact {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String contactType;
	private String contactValue;
	private LocalDateTime updatedTS;
	private String updatedBy;
	private Long version;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "personContact", cascade = CascadeType.ALL, optional = true, fetch=FetchType.EAGER)
	private ContactAddress contactAddress;

	public PersonContact(String contactType, String contactValue, LocalDateTime updatedTS, String updatedBy,
			Long version) {
		super();
		this.contactType = contactType;
		this.contactValue = contactValue;
		this.updatedTS = updatedTS;
		this.updatedBy = updatedBy;
		this.version = version;
	}
	
	public void addAddress(ContactAddress address) {
		address.setPersonContact(this);
		contactAddress = address;
	}
	
	public void addPerson(Person person) {
		this.person = person;
		this.person.addContact(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactValue() {
		return contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}

	public LocalDateTime getUpdatedTS() {
		return updatedTS;
	}

	public void setUpdatedTS(LocalDateTime updatedTS) {
		this.updatedTS = updatedTS;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ContactAddress getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(ContactAddress contactAddress) {
		this.contactAddress = contactAddress;
	}
	
	
	
}
