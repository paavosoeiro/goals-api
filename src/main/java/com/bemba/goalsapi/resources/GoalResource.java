package com.bemba.goalsapi.resources;

import org.springframework.hateoas.core.Relation;

import com.bemba.goalsapi.entities.Goal;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Relation(value = "goal", collectionRelation = "goals")
public class GoalResource extends ResourceWithEmbeddeds {

	@Getter
	private Goal goal;

	@JsonCreator
	public GoalResource(Goal goal) {
		super();
		this.goal = goal;
	}

}
