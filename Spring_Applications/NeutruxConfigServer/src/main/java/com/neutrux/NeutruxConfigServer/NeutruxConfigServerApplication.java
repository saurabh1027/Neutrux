package com.neutrux.NeutruxConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class NeutruxConfigServerApplication extends SpringBootServletInitializer {

	@Override  
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
		return application.sources(NeutruxConfigServerApplication.class);  
	}
	
	public static void main(String[] args) {
		SpringApplication.run(NeutruxConfigServerApplication.class, args);
	}

}
