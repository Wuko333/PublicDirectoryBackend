package com.sligobaptistchurch.directory.backend.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sligobaptistchurch.directory.backend.assembler.AddressResourceAssembler;
import com.sligobaptistchurch.directory.backend.assembler.ContactResourceAssembler;
import com.sligobaptistchurch.directory.backend.assembler.PersonResourceAssembler;
import com.sligobaptistchurch.directory.backend.domain.ContactAddress;
import com.sligobaptistchurch.directory.backend.domain.Person;
import com.sligobaptistchurch.directory.backend.domain.PersonContact;
import com.sligobaptistchurch.directory.backend.services.EntityService;

import javax.validation.Valid;

@RestController
@RequestMapping
@CrossOrigin
public class EntityController {
	@Autowired
	private EntityService entityService;
	private PersonResourceAssembler personResourceAssembler;
	private ContactResourceAssembler contactResourceAssembler;
	private AddressResourceAssembler addressResourceAssembler;

	public EntityController(EntityService entityService, PersonResourceAssembler personResourceAssembler,
			ContactResourceAssembler contactResourceAssembler, AddressResourceAssembler addressResourceAssembler) {
//		this.entityService = entityService;
		this.personResourceAssembler = personResourceAssembler;
		this.contactResourceAssembler = contactResourceAssembler;
		this.addressResourceAssembler = addressResourceAssembler;
	}

	// Get
	@GetMapping("/person")
	public CollectionModel<EntityModel<Person>> getAllPeople() {
		return new CollectionModel<>(entityService.getAllPeople().stream()
				.map(person -> personResourceAssembler.toModel(person)).collect(Collectors.toList()),
				new Link("http://localhost:8080/person").withRel("createPerson"));
	}

	@GetMapping("/person/search/{query}")
	public CollectionModel<EntityModel<Person>> findByName(@PathVariable String query) {
		return new CollectionModel<>(entityService.findByName(query).stream()
				.map(person -> personResourceAssembler.toModel(person)).collect(Collectors.toList()),
				new Link("http://localhost:8080/person").withRel("createPerson"));
	}
	
	@GetMapping("/address/search/{query}")
	public CollectionModel<EntityModel<Person>> findByAddress(@PathVariable String query) {
		return new CollectionModel<>(entityService.findByAddress(query).stream()
				.map(person -> personResourceAssembler.toModel(person)).collect(Collectors.toList()),
				new Link("http://localhost:8080/person").withRel("createPerson"));
	}

	@GetMapping("/person/{pid}")
	public EntityModel<?> getPerson(@PathVariable Long pid) {
		return personResourceAssembler.toModel(entityService.findPersonById(pid));
	}

	@GetMapping("/person/{pid}/contact")
	public CollectionModel<EntityModel<PersonContact>> getAllContacts(@PathVariable Long pid) {

		return new CollectionModel<>(
				entityService.getAllContacts(pid).stream().map(contact -> contactResourceAssembler.toModel(contact))
						.collect(Collectors.toList()),
				new Link("http://localhost:8080/person/{pid}/contact").withRel("createContact"));
	}

	@GetMapping("/person/{pid}/contact/{cid}")
	public EntityModel<?> getContact(@PathVariable Long cid) {
		return contactResourceAssembler.toModel(entityService.findContactById(cid));
	}

	@GetMapping("/person/{pid}/contact/{cid}/address/{aid}")
	public EntityModel<?> getAddress(@PathVariable Long pid, @PathVariable Long cid, @PathVariable Long aid) {
		return addressResourceAssembler.toModel(entityService.getAddressById(aid));
	}

	// Post
	@PostMapping("/person")
	public Object createPerson(@Valid @RequestBody Person person, BindingResult result) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return personResourceAssembler.toModel(entityService.savePerson(person));
	}

	@PostMapping("/person/{pid}")
	public Object createContact(@Valid @RequestBody PersonContact contact, BindingResult result,
			@PathVariable Long pid) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return contactResourceAssembler.toModel(entityService.saveContact(contact, pid));
	}

	@PostMapping("/person/{pid}/contact/{cid}")
	public Object createAddress(@Valid @RequestBody ContactAddress address, BindingResult result,
			@PathVariable Long pid, @PathVariable Long cid) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return addressResourceAssembler.toModel(entityService.saveAddress(address, cid));
	}

	// Put
	@PutMapping("/person/{pid}")
	public Object updatePerson(@PathVariable Long pid, @Valid @RequestBody Person person, BindingResult result) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return personResourceAssembler.toModel(entityService.updatePerson(pid, person));
	}
	
	@PutMapping("/person/{pid}/contact/{cid}")
	public Object updateContact(@PathVariable Long pid, @PathVariable Long cid, 
			@Valid @RequestBody PersonContact contact,
			BindingResult result) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return contactResourceAssembler.toModel(entityService.updateContact(cid, contact));
	}
	
	@PutMapping("/person/{pid}/contact/{cid}/address/{aid}")
	public Object updateAddress(@PathVariable Long pid, @PathVariable Long cid,
			@PathVariable Long aid, @Valid @RequestBody ContactAddress address, BindingResult result) {
		if (result.hasErrors())
			return entityService.errorMap(result);

		return addressResourceAssembler.toModel(entityService.updateAddress(aid, address));
	}

	@DeleteMapping("/person/{pid}")
	public ResponseEntity<?> deletePerson(@PathVariable Long pid) {
		entityService.deletePersonById(pid);
		return new ResponseEntity<String>("Person Deleted", HttpStatus.OK);
	}
	
	@DeleteMapping("/person/{pid}/contact/{cid}")
	public ResponseEntity<?> deleteContact(@PathVariable Long cid) {
		entityService.deleteContactById(cid);
		return new ResponseEntity<String>("Contact Deleted", HttpStatus.OK);
	}
	
	@DeleteMapping("/person/{pid}/contact/{cid}/address/{aid}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long pid, @PathVariable Long cid, @PathVariable Long aid) {
		entityService.deleteAddressById(aid);
		return new ResponseEntity<String>("Address Deleted", HttpStatus.OK);
	}

}
