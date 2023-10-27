package com.example.springsecurityexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringSecurityExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityExamplesApplication.class, args);
	}

	@RestController
	class Controller extends Resource {


	}

	abstract class Resource {

		@GetMapping("/")
		@PreAuthorize("hasRole('ADMIN')")
		public String hello() {
			return "hello";
		}

	}

}
