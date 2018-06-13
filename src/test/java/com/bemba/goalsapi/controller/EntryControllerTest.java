package com.bemba.goalsapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.entities.Reward;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.EntryRepository;
import com.bemba.goalsapi.repository.GoalRepository;
import com.bemba.goalsapi.repository.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class EntryControllerTest {

	private static final String REWARD_NAME = "Reward Goal Test";

	private static final String PERSON_NAME = "Person Name";

	private MockMvc mockMvc;

	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private PersonRepository personRepository;

	private Goal goal;

	private Person person;

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		this.person = personRepository.save(createPerson(PERSON_NAME));
		this.goal = goalRepository.save(createGoal("Goal Test", "Goal Description", REWARD_NAME));
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
	public void testGoalNotFound() throws Exception {
		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(Double.valueOf(2));
		String json = json(entryDto);
		mockMvc.perform(post("/goals/948327/entry").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound());
	}

	@Test
	public void testAdd() throws Exception {
		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(Double.valueOf(2));
		String json = json(entryDto);
		mockMvc.perform(post("/goals/" + goal.getId() + "/entries").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	public void testAddOverdue() throws Exception {
		Goal goal = createGoal("Goal Overdued", "This goal is overdue", "Reward for overdue goal");
		goal.setDeadline(LocalDate.of(2018, 06, 10));
		goal = goalRepository.save(goal);

		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(Double.valueOf(2));

		String json = json(entryDto);

		mockMvc.perform(post("/goals/" + goal.getId() + "/entries").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.goal.goal.status", is("OVERDUED")));
	}

	@Test
	public void testAddFinished() throws Exception {
		EntryDto entryDto = creaetEntryDto();
		entryDto.setHours(Double.valueOf(10));

		String json = json(entryDto);

		mockMvc.perform(post("/goals/" + goal.getId() + "/entries").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.goal.goal.status", is("FINISHED")));
	}

	@Test
	public void testGetAll() throws Exception {
		List<Entry> entries = createEntries(goal, Double.valueOf(1), Double.valueOf(2), Double.valueOf(4));

		entryRepository.saveAll(entries);

		mockMvc.perform(get("/goals/" + goal.getId() + "/entries").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$._embedded.entries", hasSize(3)));
	}

	private Goal createGoal(String name, String description, String rewardName) {
		Goal goal = new Goal();
		goal.setName(name);
		goal.setDescription(description);
		goal.setDeadline(LocalDate.of(2018, 06, 20));
		goal.setTotalHours(Double.valueOf(10));
		goal.setRemainingHours(Double.valueOf(10));
		goal.setStatus(GoalStatusEnum.OPENED);

		Reward reward = new Reward();
		reward.setName(rewardName);
		goal.setReward(reward);

		goal.setPerson(person);

		return goal;
	}

	private EntryDto creaetEntryDto() {
		EntryDto entryDto = new EntryDto();
		return entryDto;
	}

	private List<Entry> createEntries(Goal goal, Double... hours) {
		List<Entry> entries = new ArrayList<>();

		for (Double hour : hours) {
			Entry entry = new Entry();
			entry.setHours(hour);
			entry.setGoal(goal);
			entries.add(entry);
		}

		return entries;
	}

	private Person createPerson(String name) {
		Person p = new Person();
		p.setName(name);
		return p;
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
