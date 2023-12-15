package com.example.springsecurityexamples;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.MatcherSecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.handler.DefaultWebFilterChain;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SecurityTestExecutionListeners
public class MinimalTest {

	@Test
	@WithMockUser
	void minimal() {
		List<WebFilter> sortedWebFilters = new ArrayList<>();
		sortedWebFilters.add(new ReactorContextWebFilter(new WebSessionServerSecurityContextRepository()));
		sortedWebFilters.add(new AuthorizationWebFilter(AuthenticatedReactiveAuthorizationManager.authenticated()));
		var springSecurityFilterChain = new WebFilterChainProxy(new MatcherSecurityWebFilterChain(ServerWebExchangeMatchers.anyExchange(), sortedWebFilters));
		WebHandler handler = (e) -> {
			e.getResponse().setStatusCode(HttpStatus.OK);
			return Mono.empty();
		};
		var wfc = new DefaultWebFilterChain(handler, Arrays.asList(springSecurityFilterChain));
		var request = MockServerHttpRequest.get("/").build();
		MockServerWebExchange exchange = MockServerWebExchange.builder(request)
				.build();

		wfc.filter(exchange).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
