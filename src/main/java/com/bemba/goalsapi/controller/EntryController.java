package com.bemba.goalsapi.controller;

import java.time.LocalDate;
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

import com.bemba.goalsapi.dto.EntryDto;
import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;

@RestController
@RequestMapping("/goal/{id}/entry")
public class EntryController {

	private static final Logger log = LoggerFactory.getLogger(EntryController.class);

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private ModelMapper mapper;

	@PostMapping
	public ResponseEntity<Entry> add(@PathVariable("id") Long id, @RequestBody EntryDto entryDto) {

		Optional<Goal> goalOpt = goalRepository.findById(id);

		if (!goalOpt.isPresent()) {
			log.info("Not found any goal with id: {}", id);
			return ResponseEntity.notFound().build();
		}

		Goal goal = goalOpt.get();
		Entry entry = mapper.map(entryDto, Entry.class);

		goal.setRemainingHours(goal.getRemainingHours() - entry.getHours());

		if (goal.isFinished()) {
			log.info("Goal {} is finished", goal.getName());
			goal.setStatus(GoalStatusEnum.FINISHED);
			goal.setEndDate(LocalDate.now());
		} else {
			if (goal.isOverdue()) {
				log.info("Goal {} is overdue", goal.getName());
				goal.setStatus(GoalStatusEnum.OVERDUED);
			}

		}

		entry.setGoal(goal);
		log.info("Saving entry {} for goal {}", entry, goal.getName());
		Entry save = entryRepository.save(entry);
		return ResponseEntity.ok(save);
	}

	@GetMapping
	public ResponseEntity<List<Entry>> getAll(@PathVariable("id") Long goalId) {
		log.info("Retrieving all entries for GoalId: {}", goalId);
		List<Entry> all = entryRepository.findByGoalId(goalId);
		return ResponseEntity.ok(all);
	}

}
