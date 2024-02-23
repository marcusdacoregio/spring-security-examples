package com.example.springsecurityexamples;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(SpringSecurityExamplesApplication.Hints.class)
public class SpringSecurityExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityExamplesApplication.class, args);
	}

	static class Hints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.reflection().registerType(Authz.class, MemberCategory.INVOKE_DECLARED_METHODS);
		}

	}

}
