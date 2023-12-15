package com.example.springsecurityexamples;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import reactor.util.context.ReactorContextAccessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityExamplesApplicationTests {

	@Autowired
	WebTestClient client;

	@Autowired
	WebFilter springSecurityFilterChain;

	@Test
	@WithMockUser
	void withMockUserShouldWork() {
//		var request = MockServerHttpRequest.get("/").build();
//		MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();
//		this.springSecurityFilterChain.filter(exchange, (e) -> Mono.empty()).block();
//
//		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);

		this.client.get().uri("/")
//				.headers((headers) -> headers.setBasicAuth("user", "password"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("index");
	}

	@TestConfiguration
	static class Config {

		@Bean
		WebTestClientBuilderCustomizer webTestClientBuilderCustomizer() {
			return builder -> builder.responseTimeout(Duration.ofMinutes(5));
		}

	}

}
