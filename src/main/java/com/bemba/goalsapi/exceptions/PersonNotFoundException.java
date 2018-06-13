package com.bemba.goalsapi.exceptions;

public class PersonNotFoundException extends RuntimeException {

	public PersonNotFoundException(String personId) {
		super("could not find user " + personId + ".");
	}

}
