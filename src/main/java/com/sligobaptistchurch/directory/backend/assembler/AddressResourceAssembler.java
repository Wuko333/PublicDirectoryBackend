package com.sligobaptistchurch.directory.backend.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.sligobaptistchurch.directory.backend.controllers.EntityController;
import com.sligobaptistchurch.directory.backend.domain.ContactAddress;

@Component
public class AddressResourceAssembler
		implements RepresentationModelAssembler<ContactAddress, EntityModel<ContactAddress>> {

	@Override
	public EntityModel<ContactAddress> toModel(ContactAddress entity) {
		Long personId = entity.getPersonContact().getPerson().getId();
		Long contactId = entity.getPersonContact().getId();
		return new EntityModel<>(entity,
				linkTo(methodOn(EntityController.class).getAddress(personId, contactId, entity.getId())).withRel("getThisAddress"),
				linkTo(methodOn(EntityController.class).deleteAddress(personId, contactId, entity.getId())).withRel("deleteThisAddress"),
				new Link("http://localhost:8080/dashboard/" + entity.getId()).withRel("updateThisAddress"));
	}
}
