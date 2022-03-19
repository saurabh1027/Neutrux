package com.neutrux.NeutruxConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class NeutruxConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxConfigServerApplication.class, args);
	}

}
