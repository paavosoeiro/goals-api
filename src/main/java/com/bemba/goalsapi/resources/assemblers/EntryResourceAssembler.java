package com.bemba.goalsapi.resources.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.stereotype.Service;

import com.bemba.goalsapi.controller.EntryController;
import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.resources.EntryResource;
import com.bemba.goalsapi.resources.GoalResource;

@Service
public class EntryResourceAssembler extends EmbeddableResourceAssemblerSupport<Entry, EntryResource, EntryController> {

	@Autowired
	private GoalResourceAssembler goalResourceAssembler;

	public EntryResourceAssembler(EntityLinks entityLinks, RelProvider relProvider) {
		super(entityLinks, relProvider, EntryController.class, EntryResource.class);
	}

	@Override
	public EntryResource toResource(Entry entity) {
		EntryResource entryResource = createResourceWithId(entity.getId(), entity, entity.getGoal().getId());
		String resourceRelFor = relProvider.getItemResourceRelFor(GoalResource.class);
		entryResource.add(goalResourceAssembler.linkToSingleResource(entity.getGoal()).withRel(resourceRelFor));
		return entryResource;
	}

	public EntryResource toDetailedResource(Entry entity) {
		EntryResource entryResource = createResourceWithId(entity.getId(), entity, entity.getGoal().getId());

		final List<EmbeddedWrapper> embeddables = new ArrayList<EmbeddedWrapper>();

		embeddables.add(goalResourceAssembler.toEmbeddable((entity.getGoal())));
		entryResource.setEmbeddeds(new Resources<>(embeddables));
		return entryResource;
	}

	@Override
	public Resources<EntryResource> toEmbeddedListWithId(Iterable<Entry> entities, Long id) {
		List<EntryResource> resources = toResources(entities);
		return new Resources<EntryResource>(resources,
				linkTo(methodOn(EntryController.class).getAll(id)).withSelfRel());
	}

	@Override
	public Link linkToSingleResource(Entry entity) {
		return entityLinks.linkToSingleResource(Entry.class, entity.getId());
	}

	@Override
	protected EntryResource instantiateResource(Entry entity) {
		return new EntryResource(entity);
	}

}
