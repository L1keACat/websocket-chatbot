package com.likeacat.websocketchatbot;

import com.likeacat.websocketchatbot.services.MessageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebsocketChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChatbotApplication.class, args);
	}

	/*@Bean
	CommandLineRunner init() {
		return args -> {

		};
	}*/

}
