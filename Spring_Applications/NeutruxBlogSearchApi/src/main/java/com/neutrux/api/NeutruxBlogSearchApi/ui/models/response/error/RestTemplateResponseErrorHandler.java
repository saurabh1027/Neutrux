package com.neutrux.api.NeutruxBlogSearchApi.ui.models.response.error;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (
				response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR 
          || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode()
			.series() == HttpStatus.Series.SERVER_ERROR) {
			System.err.println("server_error");
        } else if (response.getStatusCode()
    		.series() == HttpStatus.Series.CLIENT_ERROR) {
        	System.err.println("client_error");
        }
	}

}