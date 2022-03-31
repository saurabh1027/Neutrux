package com.neutrux.api.NeutruxUsersApi.configurations;

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

import com.neutrux.api.NeutruxUsersApi.security.AuthorizationFilter;
import com.neutrux.api.NeutruxUsersApi.service.UsersService;

/*
	You can remove @Configuration annotation from here. It will cause any changes
	Because @EnableWebSecurity already has @Configuration annotation applied on it.
	You can check it by doing ctrl+click on @EnableWebSecurity annotation below.
	
	The prePostEnabled property enables Spring Security pre/post annotations.
	The securedEnabled property determines if the @Secured annotation should be enabled.
	The jsr250Enabled property allows us to use the @RoleAllowed annotation.
*/

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
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/users/").permitAll()
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