package com.bemba.goalsapi.resources;

import org.springframework.hateoas.core.Relation;

import com.bemba.goalsapi.entities.Person;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Relation(value = "person", collectionRelation = "persons")
public class PersonResource extends ResourceWithEmbeddeds {

	@Getter
	private Person person;

	@JsonCreator
	public PersonResource(Person person) {
		super();
		this.person = person;
	}

}
