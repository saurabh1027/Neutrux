package com.neutrux.api.NeutruxUsersApi.exceptions;

import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.neutrux.api.NeutruxUsersApi.ui.models.InvalidField;
import com.neutrux.api.NeutruxUsersApi.ui.models.response.ErrorMessageResponseModel;
import com.neutrux.api.NeutruxUsersApi.ui.models.response.MethodArgumentNotValidExceptionResponseModel;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

	/*
	 * 
	 * It will not handle exceptions that are overriden by class. You need to
	 * override those methods and then define each method that is needed!
	 * 
	 */

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Set<InvalidField> invalidFields = new HashSet<InvalidField>();

		for (FieldError fieldError : ex.getFieldErrors()) {
			invalidFields.add(new InvalidField(fieldError.getField(), fieldError.getDefaultMessage()));
		}

		MethodArgumentNotValidExceptionResponseModel responseModel = new MethodArgumentNotValidExceptionResponseModel(
				new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, invalidFields);

		return new ResponseEntity<Object>(responseModel, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

		String errorMessageDescription = ex.getMessage();

		ErrorMessageResponseModel errorMessage = buildErrorMessage(ex, errorMessageDescription, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getType());

	}
	
	@ExceptionHandler({ NoSuchElementException.class })
	public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request){
		
		String errorMessageDescription = "User Not Found";

		ErrorMessageResponseModel errorMessage = buildErrorMessage(ex, errorMessageDescription, HttpStatus.NOT_FOUND);

		return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getType());
		
	}

	public ErrorMessageResponseModel buildErrorMessage(Exception ex, String errorMessageDescription, HttpStatus httpStatus) {

		if (errorMessageDescription == null || errorMessageDescription.isEmpty()) {
			errorMessageDescription = ex.toString();
		}

		ErrorMessageResponseModel errorMessage = new ErrorMessageResponseModel(new Date(), httpStatus.value(), httpStatus,
				errorMessageDescription);

		return errorMessage;

	}

}