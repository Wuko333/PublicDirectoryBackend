package com.sligobaptistchurch.directory.backend.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.sligobaptistchurch.directory.backend.controllers.EntityController;
import com.sligobaptistchurch.directory.backend.domain.Person;


@Component
public class PersonResourceAssembler implements RepresentationModelAssembler<Person, EntityModel<Person>>{
	
	@Override
	public EntityModel<Person> toModel(Person entity) {
		return new EntityModel<>(entity,
				linkTo(methodOn(EntityController.class).getPerson(entity.getId())).withRel("getThisPerson"),
				linkTo(methodOn(EntityController.class).deletePerson(entity.getId())).withRel("deleteThisPerson"),
				linkTo(methodOn(EntityController.class).getAllPeople()).withRel("getAllPeople"),
				new Link("http://localhost:8080/person/"+entity.getId()).withRel("updateThisPerson")
				);
	}
}

