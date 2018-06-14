package com.bemba.goalsapi.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.dto.EntryDto;
import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.exceptions.GoalNotFoundException;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;
import com.bemba.goalsapi.resources.EntryResource;
import com.bemba.goalsapi.resources.assemblers.EntryResourceAssembler;

@RestController
@ExposesResourceFor(EntryResource.class)
@RequestMapping("/goals/{id}/entries")
public class EntryController {

	private static final Logger log = LoggerFactory.getLogger(EntryController.class);

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private ModelMapper mapper;

	private EntryResourceAssembler entryResourceAssembler;

	@Autowired
	public EntryController(EntryResourceAssembler entryResourceAssembler) {
		this.entryResourceAssembler = entryResourceAssembler;
	}

	@PostMapping
	public ResponseEntity<EntryResource> add(@PathVariable("id") Long id, @RequestBody EntryDto entryDto) {

		validateGoal(id);

		Optional<Goal> goalOpt = goalRepository.findById(id);

		Goal goal = goalOpt.get();
		Entry entry = mapper.map(entryDto, Entry.class);

		goal.setRemainingHours(goal.getRemainingHours() - entry.getHours());

		if (goal.isFinished()) {
			log.info("Goal {} is finished", goal.getName());
			goal.setStatus(GoalStatusEnum.FINISHED);
			goal.setEndDate(LocalDate.now());
			Person person = goal.getPerson();
			person.getRewards().add(goal.getReward());
		} else {
			if (goal.isOverdue()) {
				log.info("Goal {} is overdue", goal.getName());
				goal.setStatus(GoalStatusEnum.OVERDUED);
			}

		}

		entry.setGoal(goal);
		log.info("Saving entry {} for goal {}", entry, goal.getName());
		Entry save = entryRepository.save(entry);

		EntryResource resource = entryResourceAssembler.toDetailedResource(save);

		return ResponseEntity.ok(resource);
	}

	@GetMapping
	public ResponseEntity<Resources<EntryResource>> getAll(@PathVariable("id") Long goalId) {
		log.info("Retrieving all entries for GoalId: {}", goalId);
		validateGoal(goalId);

		Iterable<Entry> all = entryRepository.findByGoalId(goalId);

		Resources<EntryResource> resources = entryResourceAssembler.toEmbeddedListWithId(all, goalId);

		return ResponseEntity.ok(resources);
	}

	private void validateGoal(Long goalId) {
		this.goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId.toString()));
	}

}
