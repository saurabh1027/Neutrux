package com.neutrux.api.NeutruxAuthenticationApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScans({
	@ComponentScan("com.neutrux.api.NeutruxAuthenticationApi.configurations") 
})
@EnableJpaRepositories("com.neutrux.api.NeutruxAuthenticationApi.repositories")
@EntityScan("com.neutrux.api.NeutruxAuthenticationApi.ui.models")
public class NeutruxAuthenticationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxAuthenticationApiApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedOrigins("http://localhost:4200")
					.exposedHeaders("X-Access-Token,X-User-ID");
			}
		};
	}

}
