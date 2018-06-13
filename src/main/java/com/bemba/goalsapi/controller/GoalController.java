package com.bemba.goalsapi.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.dto.GoalDto;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.exceptions.GoalNotFoundException;
import com.bemba.goalsapi.exceptions.PersonNotFoundException;
import com.bemba.goalsapi.repository.GoalRepository;
import com.bemba.goalsapi.repository.PersonRepository;
import com.bemba.goalsapi.resources.GoalResource;
import com.bemba.goalsapi.resources.assemblers.GoalResourceAssembler;

@RestController
@ExposesResourceFor(GoalResource.class)
@RequestMapping("/persons/{id}/goals")
public class GoalController {

	private static final Logger log = LoggerFactory.getLogger(GoalController.class);

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ModelMapper mapper;

	private GoalResourceAssembler goalResourceAssembler;

	@Autowired
	public GoalController(GoalResourceAssembler goalResourceAssembler) {
		this.goalResourceAssembler = goalResourceAssembler;
	}

	@PostMapping
	public ResponseEntity<GoalResource> add(@PathVariable("id") Long id, @RequestBody GoalDto goalDto) {
		log.info("Finding person for id {}", id);

		validatePerson(id);

		Optional<Person> personOpt = personRepository.findById(id);

		log.info("Adding new goal {} for: ", goalDto, personOpt.get().getName());
		Goal goal = mapper.map(goalDto, Goal.class);

		Person person = personOpt.get();

		goal.setPerson(person);
		goal.setRemainingHours(goal.getTotalHours());
		Goal save = goalRepository.save(goal);
		GoalResource goalResource = goalResourceAssembler.toResource(save);

		return ResponseEntity.ok(goalResource);
	}

	@GetMapping
	public ResponseEntity<Resources<GoalResource>> getAll(@PathVariable("id") Long id) {
		log.info("Retrieving all goals for personID: {}", id);

		validatePerson(id);

		Iterable<Goal> list = goalRepository.findByPersonId(id);

		Resources<GoalResource> resources = goalResourceAssembler.toEmbeddedListWithId(list, id);

		return ResponseEntity.ok(resources);
	}

	@GetMapping("/{goalId}")
	public ResponseEntity<GoalResource> getOne(@PathVariable("goalId") Long goalId, @PathVariable("id") Long id,
			@RequestParam(value = "detailed", required = false, defaultValue = "false") boolean detailed) {
		validatePerson(id);

		Optional<Goal> goalOpt = goalRepository.findById(goalId);

		if (!goalOpt.isPresent()) {
			throw new GoalNotFoundException(id.toString());
		}

		GoalResource resource = detailed ? goalResourceAssembler.toDetailedResource(goalOpt.get())
				: goalResourceAssembler.toResource(goalOpt.get());
		return ResponseEntity.ok(resource);

	}

	private void validatePerson(Long personId) {
		personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId.toString()));
	}

}
