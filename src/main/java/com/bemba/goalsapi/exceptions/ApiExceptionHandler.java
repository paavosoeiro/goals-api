package com.bemba.goalsapi.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler {

	@ResponseBody
	@ExceptionHandler(PersonNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected VndErrors handlePersonNotFoundException(PersonNotFoundException exception) {
		return new VndErrors("error", exception.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(GoalNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected VndErrors handleGoalNotFoundException(GoalNotFoundException exception) {
		return new VndErrors("error", exception.getMessage());
	}
}
