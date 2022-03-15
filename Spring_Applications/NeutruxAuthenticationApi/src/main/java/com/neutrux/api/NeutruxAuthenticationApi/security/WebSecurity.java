package com.neutrux.api.NeutruxAuthenticationApi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.neutrux.api.NeutruxAuthenticationApi.service.UsersService;

@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UsersService usersService;
	
	@Autowired
	public WebSecurity(
		BCryptPasswordEncoder bCryptPasswordEncoder,
		UsersService usersService	
	) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.usersService = usersService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**").permitAll()
			.and()
			.addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
	}

	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService,authenticationManager());
//		authenticationFilter.setFilterProcessesUrl("/authenticate");
		return authenticationFilter;
	}

}