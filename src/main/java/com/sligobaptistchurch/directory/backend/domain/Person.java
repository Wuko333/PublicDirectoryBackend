package com.sligobaptistchurch.directory.backend.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String firstname;
	private String lastname;
	private String middlename;
	private String nickname;
	private LocalDate birthday;
	private String gender;
	private LocalDateTime updatedTS;
	private String updatedBy;
	private Long version;
	
	@JsonManagedReference
	@OneToMany(mappedBy="person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@ElementCollection
	private Set<PersonContact> contacts = new HashSet<>();

	public Person(String firstName, String lastName, String middleName, String nickName, LocalDate birthday,
			String gender, LocalDateTime updatedTS, String updatedBy, Long version) {
		super();
		this.firstname = firstName;
		this.lastname = lastName;
		this.middlename = middleName;
		this.nickname = nickName;
		this.birthday = birthday;
		this.gender = gender;
		this.updatedTS = updatedTS;
		this.updatedBy = updatedBy;
		this.version = version;
}
	
	
	public void addContact(PersonContact contact) {
		contact.setPerson(this);
		contacts.add(contact);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getMiddlename() {
		return middlename;
	}


	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public LocalDate getBirthday() {
		return birthday;
	}


	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
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


	public Set<PersonContact> getContacts() {
		return contacts;
	}


	public void setContacts(Set<PersonContact> contacts) {
		this.contacts = contacts;
	}
	
	
}
