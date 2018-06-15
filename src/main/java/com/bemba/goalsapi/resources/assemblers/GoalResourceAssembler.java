package com.bemba.goalsapi.resources.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.stereotype.Service;

import com.bemba.goalsapi.controller.EntryController;
import com.bemba.goalsapi.controller.GoalController;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.resources.EntryResource;
import com.bemba.goalsapi.resources.GoalResource;

@Service
public class GoalResourceAssembler extends EmbeddableResourceAssemblerSupport<Goal, GoalResource, GoalController> {

	@Autowired
	private PersonResourceAssembler personResourceAssembler;

	@Autowired
	private EntryResourceAssembler entryResourceAssembler;

	public GoalResourceAssembler(EntityLinks entityLinks, RelProvider relProvider) {
		super(entityLinks, relProvider, GoalController.class, GoalResource.class);
	}

	@Override
	public GoalResource toResource(Goal entity) {
		GoalResource goalResource = createResourceWithId(entity.getId(), entity, entity.getPerson().getId());
		goalResource.add(personResourceAssembler.linkToSingleResource(entity.getPerson()).withRel("person"));

		String resourceRelFor = relProvider.getCollectionResourceRelFor(EntryResource.class);
		goalResource.add(linkTo(methodOn(EntryController.class).getAll(entity.getId())).withRel(resourceRelFor));
		return goalResource;
	}

	public GoalResource toDetailedResource(Goal entity) {
		GoalResource goalResource = createResourceWithId(entity.getId(), entity, entity.getPerson().getId());
		goalResource.add(personResourceAssembler.linkToSingleResource(entity.getPerson()).withRel("person"));

		final List<EmbeddedWrapper> embeddables = new ArrayList<EmbeddedWrapper>();
		embeddables.addAll(entryResourceAssembler.toEmbeddable(entity.getEntries()));
		goalResource.setEmbeddeds(new Resources<>(embeddables));

		return goalResource;
	}

	@Override
	public Link linkToSingleResource(Goal entity) {
		return entityLinks.linkToSingleResource(Goal.class, entity.getId());
	}

	@Override
	protected GoalResource instantiateResource(Goal entity) {
		return new GoalResource(entity);
	}

	@Override
	public Resources<GoalResource> toEmbeddedListWithId(Iterable<Goal> entities, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Resources<GoalResource> toEmbeddedListWithId(Iterable<Goal> entities, Long id) {
//		final List<GoalResource> resources = toResources(entities);
//		return new Resources<GoalResource>(resources, linkTo(methodOn(GoalController.class).getAll(id)).withSelfRel());
//	}

}
