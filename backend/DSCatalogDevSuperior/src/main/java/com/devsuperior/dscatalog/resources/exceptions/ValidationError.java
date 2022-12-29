package com.devsuperior.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError extends MyStandardError{
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();
	
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	};

}
