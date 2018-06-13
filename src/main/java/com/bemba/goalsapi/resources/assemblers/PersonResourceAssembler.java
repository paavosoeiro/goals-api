package com.bemba.goalsapi.resources.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.stereotype.Service;

import com.bemba.goalsapi.controller.GoalController;
import com.bemba.goalsapi.controller.PersonController;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.resources.GoalResource;
import com.bemba.goalsapi.resources.PersonResource;

@Service
public class PersonResourceAssembler
		extends EmbeddableResourceAssemblerSupport<Person, PersonResource, PersonController> {

	@Autowired
	private GoalResourceAssembler goalResourceAssembler;

	public PersonResourceAssembler(EntityLinks entityLinks, RelProvider relProvider) {
		super(entityLinks, relProvider, PersonController.class, PersonResource.class);
	}

	@Override
	public PersonResource toResource(Person entity) {
		final PersonResource personResource = createResourceWithId(entity.getId(), entity);
		String resourceRelFor = relProvider.getCollectionResourceRelFor(GoalResource.class);
		Link personGoals = linkTo(methodOn(GoalController.class).getAll(entity.getId())).withRel(resourceRelFor);
		personResource.add(personGoals);

		return personResource;
	}

	public PersonResource toDetailedResource(Person entity) {
		final PersonResource personResource = createResourceWithId(entity.getId(), entity);
		final List<EmbeddedWrapper> embeddables = new ArrayList<EmbeddedWrapper>();
		embeddables.addAll(goalResourceAssembler.toEmbeddable(entity.getGoals()));

		personResource.setEmbeddeds(new Resources<>(embeddables));
		return personResource;
	}

	@Override
	public Link linkToSingleResource(Person entity) {
		return entityLinks.linkToSingleResource(PersonResource.class, entity.getId());
	}

	@Override
	protected PersonResource instantiateResource(Person entity) {
		return new PersonResource(entity);
	}

	@Override
	public Resources<PersonResource> toEmbeddedListWithId(Iterable<Person> entities, Long id) {
		throw new NotYetImplementedException();
	}

}
