package com.bemba.goalsapi.resources;

import org.springframework.hateoas.core.Relation;

import com.bemba.goalsapi.entities.Entry;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Relation(value = "entry", collectionRelation = "entries")
public class EntryResource extends ResourceWithEmbeddeds {

	@Getter
	private Entry entry;

	@JsonCreator
	public EntryResource(Entry entry) {
		super();
		this.entry = entry;
	}
}
