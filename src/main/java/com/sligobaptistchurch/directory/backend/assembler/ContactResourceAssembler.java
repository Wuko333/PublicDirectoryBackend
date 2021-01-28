package com.sligobaptistchurch.directory.backend.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.sligobaptistchurch.directory.backend.controllers.EntityController;
import com.sligobaptistchurch.directory.backend.domain.PersonContact;

@Component
public class ContactResourceAssembler
		implements RepresentationModelAssembler<PersonContact, EntityModel<PersonContact>> {

	@Override
	public EntityModel<PersonContact> toModel(PersonContact entity) {
		Long personId = Long.valueOf(0);
		if(personId==0)
			personId = (long)1;
		else
			personId = entity.getPerson().getId();
		return new EntityModel<>(entity,
				linkTo(methodOn(EntityController.class).getContact(entity.getId())).withRel("getThisContact"),
				linkTo(methodOn(EntityController.class).deleteContact(entity.getId())).withRel("deleteThisContact"),
				//Change this line in the final version
				linkTo(methodOn(EntityController.class).getAllContacts(entity.getId())).withRel("getAllContacts"),
				new Link("http://localhost:8080/contact/" + entity.getId()).withRel("updateThisContact"));
	}
}
