package com.springlog.repetitivelearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringlogRepetitiveLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringlogRepetitiveLearningApplication.class, args);
	}

}
