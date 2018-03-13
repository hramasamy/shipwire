package com.example.easyboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EasybootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasybootApplication.class, args);
	}
}
