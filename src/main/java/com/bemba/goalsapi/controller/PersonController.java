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

import com.bemba.goalsapi.dto.PersonDto;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.exceptions.PersonNotFoundException;
import com.bemba.goalsapi.repository.PersonRepository;
import com.bemba.goalsapi.resources.PersonResource;
import com.bemba.goalsapi.resources.assemblers.PersonResourceAssembler;

@RestController
@ExposesResourceFor(PersonResource.class)
@RequestMapping("/persons")
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	private PersonRepository personRepository;

	private PersonResourceAssembler personResourceAssembler;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	public PersonController(PersonResourceAssembler personResourceAssembler) {
		this.personResourceAssembler = personResourceAssembler;
	}

	@PostMapping
	public ResponseEntity<PersonResource> add(@RequestBody PersonDto personDto) {
		log.info("Adding new person: {}", personDto);
		Person person = mapper.map(personDto, Person.class);
		Person save = personRepository.save(person);
		PersonResource resource = personResourceAssembler.toResource(save);
		return ResponseEntity.ok(resource);
	}

	@GetMapping
	public ResponseEntity<Resources<PersonResource>> getAll() {
		Iterable<Person> all = personRepository.findAll();
		Resources<PersonResource> list = personResourceAssembler.toEmbeddedList(all);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PersonResource> findOne(@PathVariable("id") Long id,
			@RequestParam(value = "detailed", required = false, defaultValue = "false") boolean detailed) {

		Optional<Person> personOpt = personRepository.findById(id);
		if (!personOpt.isPresent()) {
			throw new PersonNotFoundException(id.toString());
		}
		PersonResource personResource = detailed ? personResourceAssembler.toDetailedResource(personOpt.get())
				: personResourceAssembler.toResource(personOpt.get());
		return ResponseEntity.ok(personResource);

	}
}
