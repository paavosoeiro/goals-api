package com.bemba.goalsapi.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.dto.PersonDto;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ModelMapper mapper;

	@PostMapping
	public ResponseEntity<Person> add(@RequestBody PersonDto personDto) {
		log.info("Adding new person: {}", personDto);
		Person person = mapper.map(personDto, Person.class);
		personRepository.save(person);
		return ResponseEntity.ok(person);
	}

	@GetMapping
	public ResponseEntity<List<Person>> getAll() {
		return ResponseEntity.ok(personRepository.findAll());
	}
}
