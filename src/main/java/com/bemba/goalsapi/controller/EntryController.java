package com.bemba.goalsapi.controller;

import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.web.bind.annotation.RestController;

import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;

@RestController
@RequestMapping("/api/entry")
public class EntryController {
	
	private static final Logger log = LoggerFactory.getLogger(EntryController.class);

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private GoalRepository goalRepository;

	@PostMapping
	public ResponseEntity<Entry> add(@RequestBody Entry entry) {

		Optional<Goal> goal = goalRepository.findById(entry.getGoalId());

		if (!goal.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Long result = goal.get().getTotalHours() - entry.getHours();

		if (result > 0) {
			if (LocalDate.now()
					.isAfter(goal.get().getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
				;
			goal.get().setStatus(GoalStatusEnum.OVERDUE);
		}

		entry.setGoal(goal.get());

		Entry save = entryRepository.save(entry);
		return ResponseEntity.ok(save);
	}

	@GetMapping
	public ResponseEntity<List<Entry>> getAll() {
		List<Entry> all = entryRepository.findAll();
		return ResponseEntity.ok(all);
	}

}
