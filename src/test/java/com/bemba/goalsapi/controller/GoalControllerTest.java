package com.bemba.goalsapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
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

import com.bemba.goalsapi.dto.GoalDto;
import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.entities.Person;
import com.bemba.goalsapi.entities.Reward;
import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.bemba.goalsapi.repository.GoalRepository;
import com.bemba.goalsapi.repository.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class GoalControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private static final String PERSON_NAME = "Person Name";

	private MockMvc mockMvc;

	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private PersonRepository personRepository;

	private List<Goal> goals = new ArrayList<>();

	private Person person;

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		this.person = personRepository.save(createPerson(PERSON_NAME));
	}

	@After
	public void tearDown() {
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
		String json = json(createGoalDto());

		mockMvc.perform(post("/person/" + person.getId() + "/goal").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Goal Test"))
				.andExpect(jsonPath("$.reward.name").value("Reward Test"));
	}

	@Test
	public void testGetAll() throws Exception {
		this.goals.add(this.goalRepository.save(createGoal("Goal 1", "Desc 1", "Reward 1")));
		this.goals.add(this.goalRepository.save(createGoal("Goal 2", "Desc 2", "Reward 1")));

		mockMvc.perform(get("/person/" + person.getId() + "/goal").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(goals.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].reward.id", is(goals.get(0).getReward().getId().intValue())));
	}

	private GoalDto createGoalDto() {
		GoalDto goalDto = new GoalDto();
		goalDto.setName("Goal Test");
		goalDto.setDescription("Goal Description");
		goalDto.setDeadline(LocalDate.of(2018, 06, 20));
		goalDto.setTotalHours(10L);
		goalDto.setRewardName("Reward Test");
		return goalDto;
	}

	private Goal createGoal(String name, String description, String rewardName) {
		Goal goal = new Goal();
		goal.setName(name);
		goal.setDescription(description);
		goal.setDeadline(LocalDate.of(2018, 06, 20));
		goal.setTotalHours(10L);
		goal.setRemainingHours(10L);
		goal.setStatus(GoalStatusEnum.OPENED);
		goal.setPerson(person);
		Reward reward = new Reward();
		reward.setName(rewardName);

		goal.setReward(reward);

		return goal;
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
