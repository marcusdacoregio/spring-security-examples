package com.example.springsecurityexamples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain appSecurity(ServerHttpSecurity http) {
		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange((authorize) -> authorize
						.anyExchange().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.formLogin(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	MapReactiveUserDetailsService userDetailsService()  {
		var user = User
				.withUsername("user")
				.password("{noop}password")
				.authorities("ROLE_USER").build();
		var admin = User
				.withUsername("admin")
				.password("{noop}password")
				.authorities("ROLE_USER").build();

		return new MapReactiveUserDetailsService(user, admin);
	}

}
