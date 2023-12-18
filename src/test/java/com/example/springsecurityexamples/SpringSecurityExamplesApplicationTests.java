package com.example.springsecurityexamples;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
		inheritListeners = false,
		listeners = {WithSecurityContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class}
)
class SpringSecurityExamplesApplicationTests {

	private static final String CONTEXT_OPERATOR_KEY = SecurityContext.class.getName();

	@Autowired
	WebTestClient client;

	@BeforeEach
	void setup() {
		SecurityContext securityContext = TestSecurityContextHolder.getContext();
		Hooks.onEachOperator(CONTEXT_OPERATOR_KEY,
				Operators.lift((s, sub) -> new SecuritySubContext<>(sub, securityContext)));
	}

	@AfterEach
	void clean() {
		Hooks.resetOnLastOperator(CONTEXT_OPERATOR_KEY);
	}

	@Test
	@WithMockUser
	void withMockUserShouldWork() {
		this.client.get().uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("index");
	}

	private static class SecuritySubContext<T> implements CoreSubscriber<T> {

		private static String CONTEXT_DEFAULTED_ATTR_NAME = SecuritySubContext.class.getName()
				.concat(".CONTEXT_DEFAULTED_ATTR_NAME");

		private final CoreSubscriber<T> delegate;

		private final SecurityContext securityContext;

		SecuritySubContext(CoreSubscriber<T> delegate, SecurityContext securityContext) {
			this.delegate = delegate;
			this.securityContext = securityContext;
		}

		@Override
		public Context currentContext() {
			Context context = this.delegate.currentContext();
			if (context.hasKey(CONTEXT_DEFAULTED_ATTR_NAME)) {
				return context;
			}
			context = context.put(CONTEXT_DEFAULTED_ATTR_NAME, Boolean.TRUE);
			Authentication authentication = this.securityContext.getAuthentication();
			if (authentication == null) {
				return context;
			}
			Context toMerge = ReactiveSecurityContextHolder.withSecurityContext(Mono.just(this.securityContext));
			return toMerge.putAll(context.readOnly());
		}

		@Override
		public void onSubscribe(Subscription s) {
			this.delegate.onSubscribe(s);
		}

		@Override
		public void onNext(T t) {
			this.delegate.onNext(t);
		}

		@Override
		public void onError(Throwable ex) {
			this.delegate.onError(ex);
		}

		@Override
		public void onComplete() {
			this.delegate.onComplete();
		}

	}

	@TestConfiguration
	static class Config {

		@Bean
		WebTestClientBuilderCustomizer webTestClientBuilderCustomizer() {
			return builder -> builder.responseTimeout(Duration.ofMinutes(5));
		}

	}

}
