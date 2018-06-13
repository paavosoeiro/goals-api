package com.bemba.goalsapi.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoalDto {
	
	private String name;
	
	private String description;
	
	private LocalDate deadline;
	
	private Double totalHours;
	
	private String rewardName;
}
