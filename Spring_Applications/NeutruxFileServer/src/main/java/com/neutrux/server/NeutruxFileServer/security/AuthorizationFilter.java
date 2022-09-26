package com.neutrux.server.NeutruxFileServer.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.neutrux.server.NeutruxFileServer.service.UsersService;
import com.neutrux.server.NeutruxFileServer.shared.UserDto;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private Environment environment;
	private UsersService usersService;

	@Autowired
	public AuthorizationFilter(AuthenticationManager authenticationManager,
		Environment environment,
		UsersService usersService
	) {
		super(authenticationManager);
		this.environment = environment;
		this.usersService = usersService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));
		
		if (authorizationHeader == null
				|| !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));

		if (authorizationHeader == null) {
			return null;
		}

		String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");

		String userId = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token)
				.getBody().getSubject();
		
		if (userId == null) {
			return null;
		}
		
		UserDto userDto;
		try {
			userDto = usersService.getUserByUserId(userId);
		} catch (Exception e) {
			throw new NumberFormatException("User ID cannot be null!");
		}
		
		UserDetails userDetails = usersService.loadUserByUsername(userDto.getEmail());
		
		return new UsernamePasswordAuthenticationToken(userId, userDetails.getPassword(), userDetails.getAuthorities());

	}
}