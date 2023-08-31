package com.example.springsecurityexamples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.InMemoryReactiveSessionRegistry;
import org.springframework.security.core.session.ReactiveSessionRegistry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.session.PrincipalWebSessionStore;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.session.DefaultWebSessionManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain appSecurity(ServerHttpSecurity http) {
		http
				.authorizeExchange((authorize) -> authorize
						.anyExchange().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.formLogin(Customizer.withDefaults())
				.sessionManagement((sessions) -> sessions
						.concurrentSessions((concurrency) -> concurrency
								.maxSessions(1)
								.maxSessionsPreventsLogin(false)
						)
				);
		return http.build();
	}

	@Bean(WebHttpHandlerBuilder.WEB_SESSION_MANAGER_BEAN_NAME)
	DefaultWebSessionManager webSessionManager(ReactiveSessionRegistry reactiveSessionRegistry) {
		DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
		webSessionManager.setSessionStore(new PrincipalWebSessionStore(reactiveSessionRegistry));
		return webSessionManager;
	}

	@Bean
	ReactiveSessionRegistry reactiveSessionRegistry() {
		return new InMemoryReactiveSessionRegistry();
	}

}
