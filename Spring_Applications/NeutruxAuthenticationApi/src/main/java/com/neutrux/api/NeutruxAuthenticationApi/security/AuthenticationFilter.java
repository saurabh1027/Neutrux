package com.neutrux.api.NeutruxAuthenticationApi.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutrux.api.NeutruxAuthenticationApi.service.UsersService;
import com.neutrux.api.NeutruxAuthenticationApi.ui.model.AuthenticateUserRequestModel;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private UsersService usersService;

	@Autowired
	public AuthenticationFilter(
		UsersService usersService, 
		AuthenticationManager authenticationManager
	) {
		this.usersService = usersService;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AuthenticateUserRequestModel authenticateUserRequestModel =
					new ObjectMapper().readValue(request.getInputStream(), AuthenticateUserRequestModel.class);
			
			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					authenticateUserRequestModel.getEmail(),
					authenticateUserRequestModel.getPassword(),
					new ArrayList<>()
				)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
	}
	
	
	
}