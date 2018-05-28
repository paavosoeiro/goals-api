package com.bemba.goalsapi.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;

@RestController
@RequestMapping("/api/goal/{id}/entry")
public class EntryController {

	private static final Logger log = LoggerFactory.getLogger(EntryController.class);

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private GoalRepository goalRepository;

	@PostMapping
	public ResponseEntity<Entry> add(@RequestParam("id") Long id, @RequestBody Entry entry) {

		Optional<Goal> goalOpt = goalRepository.findById(id);

		if (!goalOpt.isPresent()) {
			log.info("Not found any goal with id: {}", id);
			return ResponseEntity.notFound().build();
		}

		Goal goal = goalOpt.get();

		goal.setRemainingHours(goal.getTotalHours() - entry.getHours());

		if (goal.isFinished()) {
			log.info("Goal {} is finished", goal);
			goal.setStatus(GoalStatusEnum.FINISHED);
			goal.setEndDate(LocalDate.now());
		} else {
			if (goal.isOverdue()) {
				log.info("Goal {} is overdue", goal);
				goal.setStatus(GoalStatusEnum.OVERDUED);
			}

		}

		entry.setGoal(goal);
		log.info("Saving entry {} for goal {}", entry, goal);
		Entry save = entryRepository.save(entry);
		return ResponseEntity.ok(save);
	}

	@GetMapping
	public ResponseEntity<List<Entry>> getAll() {
		log.info("Retrieving all entries");
		List<Entry> all = entryRepository.findAll();
		return ResponseEntity.ok(all);
	}

}
