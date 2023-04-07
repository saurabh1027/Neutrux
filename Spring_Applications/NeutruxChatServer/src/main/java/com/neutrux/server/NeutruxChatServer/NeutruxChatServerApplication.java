package com.neutrux.server.NeutruxChatServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@SpringBootApplication
@RestController
public class NeutruxChatServerApplication implements WebSocketMessageBrokerConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(NeutruxChatServerApplication.class, args);
	}
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello from Saurabh!";
	}
	  
}
