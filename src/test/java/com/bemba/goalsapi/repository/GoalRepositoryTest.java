package com.bemba.goalsapi.repository;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Reward;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GoalRepositoryTest {

	@Autowired
	private GoalRepository goalRepository;

	private Goal goal;

	@After
	public void tearDown() {
		goalRepository.deleteAllInBatch();
	}

	@Before
	public void setUp() {
		goal = new Goal();
		goal.setName("Goal Test");
		goal.setDescription("Goal description");
		goal.setDeadline(LocalDate.of(2018, 6, 20));
		goal.setTotalHours(20L);
	}

	@Test
	public void testAdd() {
		goal = goalRepository.save(goal);
		assertEquals(1L, goal.getId().longValue());
	}

	@Test
	public void testAddWithReward() {
		Reward reward = new Reward();
		reward.setName("Reward Test");

		goal.setReward(reward);

		goal = goalRepository.save(goal);

		assertEquals(1L, reward.getId().longValue());
	}

}
