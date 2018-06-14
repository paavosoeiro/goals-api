package com.bemba.goalsapi.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;

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

import com.bemba.goalsapi.dto.PersonDto;
import com.bemba.goalsapi.repository.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class PersonControllerTest {

	private MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private PersonRepository personRepository;

	private static final String PERSON_NAME = "Person Name";

	@Autowired
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@After
	public void tearDown() {
		this.personRepository.deleteAll();
	}

	@Test
	public void testAdd() throws Exception {
		String json = json(createPersonDto(PERSON_NAME));
		mockMvc.perform(post("/persons").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.person.name", is(PERSON_NAME)));
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	private PersonDto createPersonDto(String name) {
		PersonDto person = new PersonDto();
		person.setName(name);
		return person;
	}
	
}
