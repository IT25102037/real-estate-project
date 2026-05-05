package com.realestate.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.realestate.project.repository")
public class RealEstateProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateProjectApplication.class, args);
	}
}
