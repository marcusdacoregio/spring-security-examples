package com.example.springsecurityexamples;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
class SpringSecurityExamplesApplicationTests {

	@Autowired
	WebTestClient client;

	@Test
	void withMockUserShouldWork() {
		this.client.get().uri("/").exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("index");
	}

}
