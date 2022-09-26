package com.neutrux.server.NeutruxFileServer.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.neutrux.server.NeutruxFileServer.ui.models.response.ErrorMessageResponseModel;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

	/*
	 * 
	 * It will not handle exceptions that are overriden by class. You need to
	 * override those methods and then define each method that is needed!
	 * 
	 */
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
		String errorMessageDescription = ex.getMessage();
		ErrorMessageResponseModel errorMessage = null;

		if( errorMessageDescription.equals("Already exists") ) {
			errorMessage = buildErrorMessage(ex, errorMessageDescription, HttpStatus.CONFLICT);
		} else {
			errorMessage = buildErrorMessage(ex, errorMessageDescription, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getType());
	}

	public ErrorMessageResponseModel buildErrorMessage(Exception ex, String errorMessageDescription,
			HttpStatus httpStatus) {

		if (errorMessageDescription == null || errorMessageDescription.isEmpty()) {
			errorMessageDescription = ex.toString();
		}

		ErrorMessageResponseModel errorMessage = new ErrorMessageResponseModel(new Date(), httpStatus.value(),
				httpStatus, errorMessageDescription);

		return errorMessage;

	}

}