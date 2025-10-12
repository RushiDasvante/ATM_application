package com.example.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmSpringbootApplication.class, args);
		System.out.println("🚀 ATM System API running on http://localhost:8081/atm");
	}

}
