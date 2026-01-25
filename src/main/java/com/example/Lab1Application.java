package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Lab1Application {

	@GetMapping("/")
	public String hello() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(Lab1Application.class, args);
	}
}