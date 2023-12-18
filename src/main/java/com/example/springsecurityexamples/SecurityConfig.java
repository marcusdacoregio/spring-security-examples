package com.example.springsecurityexamples;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.web.server.MatcherSecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.context.ReactorContextWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	WebFilterChainProxy appSecurity() {
		List<WebFilter> sortedWebFilters = new ArrayList<>();
//		sortedWebFilters.add(new TestWebFilter());
		sortedWebFilters.add(new ReactorContextWebFilter(new WebSessionServerSecurityContextRepository()));
		sortedWebFilters.add(new AuthorizationWebFilter(AuthenticatedReactiveAuthorizationManager.authenticated()));
		return new WebFilterChainProxy(new MatcherSecurityWebFilterChain(ServerWebExchangeMatchers.anyExchange(), sortedWebFilters));
	}


	static class TestWebFilter implements WebFilter {

		@Override
		public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return Mono.empty().switchIfEmpty(chain.filter(exchange)).then(); // this line succeeds
//			return chain.filter(exchange); // this line makes the test fail
		}

	}

}
