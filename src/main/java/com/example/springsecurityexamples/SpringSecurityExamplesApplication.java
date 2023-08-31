package com.example.springsecurityexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringSecurityExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityExamplesApplication.class, args);
	}

	@RestController
	static class IndexController {

		@GetMapping("/")
		String index() {
			return "index";
		}

	}

}
