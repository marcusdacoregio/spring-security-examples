package com.example.springsecurityexamples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain appSecurity(ServerHttpSecurity http) {
		http
				.csrf((csrf) -> csrf.disable()) // commenting this line makes the test pass
				.authorizeExchange((authorize) -> authorize
						.anyExchange().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.formLogin(Customizer.withDefaults());
//				.addFilterAt(new CsrfWebFilter(), SecurityWebFiltersOrder.CSRF); // uncommenting this line makes the test pass
		return http.build();
	}

}
