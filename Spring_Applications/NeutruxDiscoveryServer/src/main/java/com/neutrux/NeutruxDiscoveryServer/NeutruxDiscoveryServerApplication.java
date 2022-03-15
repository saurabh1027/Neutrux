package com.neutrux.NeutruxDiscoveryServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class NeutruxDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxDiscoveryServerApplication.class, args);
	}

}
