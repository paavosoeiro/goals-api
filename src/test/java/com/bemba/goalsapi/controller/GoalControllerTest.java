package com.bemba.goalsapi.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.bemba.goalsapi.entities.Goal;
import com.bemba.goalsapi.repository.GoalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class GoalControllerTest {

	private MockMvc mockMvc;

	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private GoalRepository goalRepository;
	
	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@Autowired
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}
	
	@Test
	public void testAdd() {
		goalRepository.save(new Goal());
		fail("Not yet implemented");
	}

	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

}
