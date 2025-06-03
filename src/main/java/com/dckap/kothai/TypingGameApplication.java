package com.dckap.kothai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TypingGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(TypingGameApplication.class, args);
	}

}
