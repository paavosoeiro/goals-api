package com.bemba.goalsapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.repository.GoalRepository;

@RestController
@RequestMapping("/api/goal")
public class GoalController {

	private static final Logger log = LoggerFactory.getLogger(GoalController.class);

	@Autowired
	private GoalRepository goalRepository;

	@PostMapping
	public ResponseEntity<Goal> add(@RequestBody Goal goal) {
		log.info("Adding new goal {}", goal);
		Goal save = goalRepository.save(goal);
		return ResponseEntity.ok(save);
	}

	@GetMapping
	public ResponseEntity<List<Goal>> getAll() {
		log.info("Retrieving all goals.");
		return ResponseEntity.ok(goalRepository.findAll());
	}
	
}
