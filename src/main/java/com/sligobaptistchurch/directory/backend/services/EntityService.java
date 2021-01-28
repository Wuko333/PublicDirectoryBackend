package com.sligobaptistchurch.directory.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.sligobaptistchurch.directory.backend.domain.ContactAddress;
import com.sligobaptistchurch.directory.backend.domain.Person;
import com.sligobaptistchurch.directory.backend.domain.PersonContact;
import com.sligobaptistchurch.directory.backend.exceptions.AddressException;
import com.sligobaptistchurch.directory.backend.exceptions.ContactException;
import com.sligobaptistchurch.directory.backend.exceptions.PersonException;
import com.sligobaptistchurch.directory.backend.repository.ContactAddressRepository;
import com.sligobaptistchurch.directory.backend.repository.PersonContactRepository;
import com.sligobaptistchurch.directory.backend.repository.PersonRepository;
import com.sligobaptistchurch.directory.backend.static_methods.HelperMethods;

@Service
public class EntityService {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonContactRepository contactRepository;
	@Autowired
	private ContactAddressRepository addressRepository;

	public EntityService(PersonRepository personRepository, PersonContactRepository contactRepository,
			ContactAddressRepository addressRepository) {

	}

	// Create
	public Person savePerson(Person person) {
		return personRepository.save(person);
	}

	public PersonContact saveContact(PersonContact personContact, Long personId) {
		Person person = personRepository.findById(personId)
				.orElseThrow(() -> new PersonException("Person with ID " + personId + " Not found"));
		person.addContact(personContact);
		return contactRepository.save(personContact);
	}

	public ContactAddress saveAddress(ContactAddress contactAddress, Long contactId) {
		PersonContact contact = contactRepository.findById(contactId)
				.orElseThrow(() -> new ContactException("Contact with ID " + contactId + " Not found"));

		contact.addAddress(contactAddress);
		return addressRepository.save(contactAddress);
	}

	// Read
	public Set<Person> getAllPeople() {
		return personRepository.findAll().stream().collect(Collectors.toSet());
	}

	public Set<PersonContact> getAllContacts(Long personId) {
		return contactRepository.findByPerson(personRepository.findById(personId)
				.orElseThrow(() -> new PersonException("Person with ID " + personId + " Not found")));
	}

	public Person findPersonById(Long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new PersonException("Person with ID " + id + " Not found"));
	}

	public PersonContact findContactById(Long id) {
		return contactRepository.findById(id)
				.orElseThrow(() -> new ContactException("Contact with ID " + id + " Not found"));
	}

	public ContactAddress getAddressById(Long id) {
		return addressRepository.findById(id)
				.orElseThrow(() -> new AddressException("Address with ID " + id + " Not found"));
	}

	// Delete
	public void deletePersonById(Long id) {
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new PersonException("Person with ID: " + id + " Not found"));
		Set<PersonContact> contacts = getAllContacts(person.getId());
		for (PersonContact contact : contacts) {
			if (contact.getContactAddress() != null) {
				ContactAddress adr = contact.getContactAddress();
				adr.setPersonContact(null);
				deleteAddressById(adr.getId());
			}
			contact.setPerson(null);
			deleteContactById(contact.getId());
		}
		personRepository.delete(person);
	}

	public void deleteContactById(Long id) {
		PersonContact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ContactException("Contact with ID: " + id + " Not found"));
		deleteAddressById(contact.getContactAddress().getId());
		contactRepository.delete(contact);
	}

	public void deleteAddressById(Long id) {
		addressRepository.delete(addressRepository.findById(id)
				.orElseThrow(() -> new AddressException("Address with ID: " + id + " Not found")));
	}

	// Update
	public Person updatePerson(Long id, Person person) {
		return personRepository.findById(id).map(per -> {
			per.setFirstname(person.getFirstname());
			per.setMiddlename(person.getMiddlename());
			per.setLastname(person.getLastname());
			per.setNickname(person.getNickname());
			per.setGender(person.getGender());
			per.setBirthday(person.getBirthday());
			per.setUpdatedBy(person.getUpdatedBy());
			per.setUpdatedTS(person.getUpdatedTS());
			per.setVersion(person.getVersion());
			per.setContacts(person.getContacts());
			return personRepository.save(per);
		}).orElseGet(() -> {
			return personRepository.save(person);
		});
	}

	public PersonContact updateContact(Long id, PersonContact contact) {
		return contactRepository.findById(id).map(con -> {
			con.setContactType(contact.getContactType());
			con.setContactAddress(contact.getContactAddress());
			con.setContactValue(contact.getContactValue());
			con.setPerson(contact.getPerson());
			con.setUpdatedBy(contact.getUpdatedBy());
			con.setUpdatedTS(contact.getUpdatedTS());
			con.setVersion(contact.getVersion());
			return contactRepository.save(con);

		}).orElseGet(() -> {
			return contactRepository.save(contact);
		});
	}

	public ContactAddress updateAddress(Long id, ContactAddress address) {
		return addressRepository.findById(id).map(adr -> {
			adr.setAddressline1(address.getAddressline1());
			adr.setAddressline2(address.getAddressline2());
			adr.setCity(address.getCity());
			adr.setCountry(address.getCountry());
			adr.setCountryCode(address.getCountryCode());
			adr.setPersonContact(address.getPersonContact());
			adr.setProvince(address.getProvince());
			adr.setState(address.getState());
			adr.setZipcode(address.getZipcode());
			return addressRepository.save(adr);

		}).orElseGet(() -> {
			return addressRepository.save(address);
		});
	}

	// Search
	public Set<Person> findByName(String name) {

		// Splitting the query string by space
		String[] splitted = name.split("\\s+");
		ArrayList<Set<Person>> personSets = new ArrayList<>();

		// personSets.get(i) contains the Set<Person> which would have been queried
		// by splitted[i]
		for (String s : splitted) {
			personSets.add(singleQuery(s));
		}

		if (splitted.length == 1) {
			return personSets.get(0);
		} else {

			Set<Person> resultSet = personSets.get(0);
			for (int i = 1; i < personSets.size(); i++) {
				resultSet.retainAll(personSets.get(i));
			}

			return resultSet;
		}
	}

	public Set<Person> findByAddress(String address) {
		String[] splitted = address.split("\\s+");
		ArrayList<Set<ContactAddress>> addressSets = new ArrayList<>();

		for (String s : splitted) {
			addressSets.add(addressQuery(s));
		}

		Set<Person> resultSet = new HashSet<>();
		if (splitted.length == 1) {

			Set<ContactAddress> addresses = addressSets.get(0);
			for (ContactAddress adr : addresses) {
				PersonContact contact = contactRepository.findById(adr.getPersonContact().getId()).get();
				Person person = personRepository.findById(contact.getPerson().getId()).get();
//				resultSet.add(adr.getPersonContact().getPerson());
				resultSet.add((Person)Hibernate.unproxy(person));
			}
			return resultSet;
		} else {
			Set<ContactAddress> resultAddresses = addressSets.get(0);
			for (int i = 1; i < addressSets.size(); i++)
				resultAddresses.retainAll(addressSets.get(i));
			for (ContactAddress adr : resultAddresses) {
				PersonContact contact = contactRepository.findById(adr.getPersonContact().getId()).get();
				Person person = personRepository.findById(contact.getPerson().getId()).get();
				resultSet.add((Person)Hibernate.unproxy(person));
			}
			return resultSet;
		}
	}

	public ResponseEntity<?> errorMap(BindingResult result) {
		Map<String, String> errorM = new HashMap<>();

		for (FieldError error : result.getFieldErrors()) {
			errorM.put(error.getField(), error.getDefaultMessage());
		}
		return new ResponseEntity<>(errorM, HttpStatus.BAD_REQUEST);
	}

	// Helper methods for search
	private Set<Person> singleQuery(String name) {
		Set<Person> unionSet = new HashSet<>();
		return HelperMethods.findUnion(unionSet, personRepository.findByFirstnameContainingIgnoreCase(name),
				personRepository.findByMiddlenameContainingIgnoreCase(name),
				personRepository.findByLastnameContainingIgnoreCase(name),
				personRepository.findByNicknameContainingIgnoreCase(name));
	}

	private Set<ContactAddress> addressQuery(String address) {
		Set<ContactAddress> unionSet = new HashSet<>();
		if (address.matches(".*\\d.*")) {
			return HelperMethods.findUnion(unionSet, addressRepository.findByAddressline1ContainingIgnoreCase(address),
					addressRepository.findByAddressline2ContainingIgnoreCase(address),
					addressRepository.findByCityContainingIgnoreCase(address),
					addressRepository.findByProvinceContainingIgnoreCase(address),
					addressRepository.findByStateContainingIgnoreCase(address),
					addressRepository.findByZipcode(Short.valueOf(address)));
		} else {
			return HelperMethods.findUnion(unionSet, addressRepository.findByAddressline1ContainingIgnoreCase(address),
					addressRepository.findByAddressline2ContainingIgnoreCase(address),
					addressRepository.findByCityContainingIgnoreCase(address),
					addressRepository.findByProvinceContainingIgnoreCase(address),
					addressRepository.findByStateContainingIgnoreCase(address));
		}

	}
}
