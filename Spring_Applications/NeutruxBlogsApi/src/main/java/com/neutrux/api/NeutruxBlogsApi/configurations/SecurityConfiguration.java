package com.neutrux.api.NeutruxBlogsApi.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neutrux.api.NeutruxBlogsApi.security.AuthorizationFilter;
import com.neutrux.api.NeutruxBlogsApi.service.UsersService;

@Configuration
@EnableWebSecurity
@ComponentScan
@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UsersService usersService;
	private Environment environment;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, UsersService usersService,
			Environment environment) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.usersService = usersService;
		this.environment = environment;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors().and().csrf().disable();
		http
			.authorizeRequests().antMatchers("/").permitAll().and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/categories/**").permitAll().and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/blogs/**").permitAll()
			.anyRequest().authenticated()
			.and()
				.addFilter(new AuthorizationFilter(authenticationManager(), environment, usersService));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}