package com.bemba.goalsapi.exceptions;

public class GoalNotFoundException extends RuntimeException {

	public GoalNotFoundException(String message) {
		super("could not find goal " + message + ".");
	}

}
