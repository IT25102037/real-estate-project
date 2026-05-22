package com.realestate.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.realestate.project", "sliit.realstate"})
public class RealEstateProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateProjectApplication.class, args);
	}

}
