package com.bemba.goalsapi.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.bemba.goalsapi.dto.EntryDto;
import com.bemba.goalsapi.entities.Entry;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Reward;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class EntryControllerTest {

	private MockMvc mockMvc;

	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private EntryRepository entryRepository;

	private Goal goal;

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		this.goal = goalRepository.save(createGoal("Goal Test", "Goal Description", "Reward Goal Test"));
	}

	@After
	public void tearDown() {
		this.entryRepository.deleteAllInBatch();
		this.goalRepository.deleteAllInBatch();
	}

	@Autowired
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}

	@Test
	public void testAdd() throws Exception {
		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(2L);
		String json = json(entryDto);
		mockMvc.perform(post("/goal/" + goal.getId() + "/entry").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.goal.remainingHours").value(8L));
	}

	@Test
	public void testAddOverdue() throws Exception {
		Goal goal = createGoal("Goal Overdued", "This goal is overdue", "Reward for overdue goal");
		goal.setDeadline(LocalDate.of(2018, 06, 10));
		goal = goalRepository.save(goal);

		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(2L);

		String json = json(entryDto);

		mockMvc.perform(post("/goal/" + goal.getId() + "/entry").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.goal.status", is("OVERDUED")));
	}

	@Test
	public void testAddFinished() throws Exception {
		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(10L);

		String json = json(entryDto);

		mockMvc.perform(post("/goal/" + goal.getId() + "/entry").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.goal.status", is("FINISHED")));
	}

	@Test
	public void testGetAll() throws Exception {
		List<Entry> entries = createEntries(goal, 1L, 2L, 4L);

		entryRepository.saveAll(entries);

		mockMvc.perform(get("/goal/" + goal.getId() + "/entry").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)));
	}

	private Goal createGoal(String name, String description, String rewardName) {
		Goal goal = new Goal();
		goal.setName(name);
		goal.setDescription(description);
		goal.setDeadline(LocalDate.of(2018, 06, 20));
		goal.setTotalHours(10L);
		goal.setRemainingHours(10L);
		goal.setStatus(GoalStatusEnum.OPENED);

		Reward reward = new Reward();
		reward.setName(rewardName);

		goal.setReward(reward);

		return goal;
	}

	private EntryDto creaetEntryDto() {
		EntryDto entryDto = new EntryDto();
		return entryDto;
	}

	private List<Entry> createEntries(Goal goal, Long... hours) {
		List<Entry> entries = new ArrayList<>();

		for (Long hour : hours) {
			Entry entry = new Entry();
			entry.setHours(hour);
			entry.setGoal(goal);
			entries.add(entry);
		}

		return entries;
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
