package com.inventory1.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void init() {
		// This forces Spring Boot to always run in Nairobi time (EAT)
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
	}

}