package com.neutrux.api.NeutruxAuthenticationApi.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutrux.api.NeutruxAuthenticationApi.service.UsersService;
import com.neutrux.api.NeutruxAuthenticationApi.shared.UserDto;
import com.neutrux.api.NeutruxAuthenticationApi.ui.models.request.AuthenticateUserRequestModel;
import com.neutrux.api.NeutruxAuthenticationApi.ui.models.response.UserResponseModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private UsersService usersService;
	private Environment environment;

	@Autowired
	public AuthenticationFilter(
		UsersService usersService, 
		Environment environment,
		AuthenticationManager authenticationManager
	) {
		this.usersService = usersService;
		this.environment = environment;
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
		
		String username = ( (UsersSecurityDetails)authResult.getPrincipal() ).getUsername();
		String tokenExpirationTime = environment.getProperty("token.expiration_time");
		
		UserDto userDto = usersService.getUserDetailsByEmail(username);
		ModelMapper modelMapper = new ModelMapper();
		
		UserResponseModel userResponseModel = modelMapper.map(userDto, UserResponseModel.class);
		Date userExpirationDate = new Date( Long.parseLong(tokenExpirationTime) );
		userResponseModel.setExpiresIn( userExpirationDate.getTime() );
		
		long currentTimeInMilli = new Date().getTime();
		long tokenExpirationTimeInMilli = Long.parseLong(tokenExpirationTime);
		
		long expiryTimeInMilli = currentTimeInMilli+tokenExpirationTimeInMilli;
		System.out.println(expiryTimeInMilli);
		Date expiryDate = new Date(expiryTimeInMilli);

		String token =
				Jwts.builder()
					.setSubject(userDto.getUserId())
//					.setExpiration(new Date( System.currentTimeMillis() + Long.parseLong(tokenExpirationTime)))
//					.setExpiration(new Date( new Date().getTime() + Long.parseLong(tokenExpirationTime)))
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
					.compact();
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(userResponseModel.toString());
		response.addHeader("X-Access-Token", token);
		response.setContentType("application/json");
	}
	
}