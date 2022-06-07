package com.neutrux.api.NeutruxUsersApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.neutrux.api.NeutruxUsersApi.ui.models.response.error.RestTemplateResponseErrorHandler;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScans({ @ComponentScan("com.neutrux.api.NeutruxUsersApi.ui.controllers"),
		@ComponentScan("com.neutrux.api.NeutruxUsersApi.configurations") })
@EnableJpaRepositories("com.neutrux.api.NeutruxUsersApi.repositories")
@EntityScan("com.neutrux.api.NeutruxUsersApi.ui.models")
//@EnableFeignClients("com.neutrux.api.NeutruxUsersApi.feign.*")
public class NeutruxUsersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxUsersApiApplication.class, args);
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
					.allowedMethods("*")
					.exposedHeaders("X-User-ID");
			}
		};
	}

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory,
			RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) throws Exception {
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setErrorHandler(restTemplateResponseErrorHandler);
		return restTemplate;
	}

	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

		factory.setReadTimeout(5000);

		factory.setConnectTimeout(15000);
		return factory;
	}

}
