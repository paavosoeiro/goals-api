package com.bemba.goalsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(basePackageClasses = { GoalsApiApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class GoalsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoalsApiApplication.class, args);
	}
}
