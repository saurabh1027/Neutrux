package com.neutrux.api.NeutruxBlogsApi;

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
	@ComponentScan("com.neutrux.api.NeutruxBlogsApi.ui.controllers"),
	@ComponentScan("com.neutrux.api.NeutruxBlogsApi.configurations"),
	@ComponentScan("com.neutrux.api.NeutruxBlogsApi.ui.models.response.error")
})
@EnableJpaRepositories("com.neutrux.api.NeutruxBlogsApi.repositories")
@EntityScan("com.neutrux.api.NeutruxBlogsApi.ui.models")
public class NeutruxBlogsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxBlogsApiApplication.class, args);
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
					.exposedHeaders("X-User-ID");
			}
		};
	}

}
