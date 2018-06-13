package com.bemba.goalsapi.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.dto.GoalDto;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.exceptions.PersonNotFoundException;
import com.bemba.goalsapi.repository.GoalRepository;
import com.bemba.goalsapi.repository.PersonRepository;

@RestController
@RequestMapping("/person/{id}/goal")
public class GoalController {

	private static final Logger log = LoggerFactory.getLogger(GoalController.class);

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ModelMapper mapper;

	@PostMapping
	public ResponseEntity<Goal> add(@PathVariable("id") Long id, @RequestBody GoalDto goalDto) {
		log.info("Finding person for id {}", id);

		validatePerson(id);

		Optional<Person> personOpt = personRepository.findById(id);

		log.info("Adding new goal {} for: ", goalDto, personOpt.get().getName());
		Goal goal = mapper.map(goalDto, Goal.class);

		Person person = personOpt.get();

		goal.setPerson(person);
		goal.setRemainingHours(goal.getTotalHours());
		Goal save = goalRepository.save(goal);

		return ResponseEntity.ok(save);
	}

	@GetMapping
	public ResponseEntity<List<Goal>> getAll(@PathVariable("id") Long id) {
		log.info("Retrieving all goals for personID: {}", id);

		validatePerson(id);

		return ResponseEntity.ok(goalRepository.findByPersonId(id));
	}

	private void validatePerson(Long personId) {
		personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId.toString()));
	}

}
