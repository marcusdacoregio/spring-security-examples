package com.example.springsecurityexamples;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Mono;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.context.ReactorContextWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@ExtendWith(SpringExtension.class)
@SecurityTestExecutionListeners
public class MinimalTest {

	@Test
	@WithMockUser
	void minimal() {
		var filter = new ReactorContextWebFilter(new WebSessionServerSecurityContextRepository());
		var request = MockServerHttpRequest.get("/").build();
		MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();
		var authorizationManager = AuthenticatedReactiveAuthorizationManager.authenticated();
		var authorizationFilter = new AuthorizationWebFilter(authorizationManager);
		MockWebFilter mockWebFilter = new MockWebFilter(filter);

		mockWebFilter.filter(exchange, ex -> {
			return authorizationFilter.filter(exchange, (exc) -> Mono.empty());
		}).block();
	}

	static class MockWebFilter implements WebFilter {

		private final WebFilter delegate;

		MockWebFilter(WebFilter delegate) {
			this.delegate = delegate;
		}

		@Override
		public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
			return this.delegate.filter(exchange, chain);
		}
	}


//	@Test
//	@WithMockUser
//	void minimal() {
//		var filter = new ReactorContextWebFilter(new WebSessionServerSecurityContextRepository());
//		var request = MockServerHttpRequest.get("/").build();
//		MockServerWebExchange exchange = MockServerWebExchange.builder(request).build();
//		var authorizationManager = AuthenticatedReactiveAuthorizationManager.authenticated();
//		var authorizationFilter = new AuthorizationWebFilter(authorizationManager);
//		filter.filter(exchange, ex -> {
//			return ReactiveSecurityContextHolder.getContext()
//					.flatMap((context) -> {
//						Assertions.assertThat(context.getAuthentication()).isNotNull();
//						return Mono.empty();
//					});
//		}).block();
//	}


}
