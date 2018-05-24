package com.bemba.goalsapi.controller;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.bemba.goalsapi.repository.GoalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EntryControllerTest {

	private MockMvc mockMvc;

	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private GoalRepository goalRepository;
	
	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAddOverdue() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

}
