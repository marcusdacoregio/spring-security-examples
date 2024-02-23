package com.example.springsecurityexamples;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpringSecurityExamplesApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void okWithAdmin() throws Exception {
		this.mvc.perform(get("/").with(httpBasic("admin", "password")))
				.andExpect(status().isOk());
	}

	@Test
	void forbiddenWithUser() throws Exception {
		this.mvc.perform(get("/").with(httpBasic("user", "password")))
				.andExpect(status().isForbidden());
	}

	@Test
	void unauthorizedWithNoAuth() throws Exception {
		this.mvc.perform(get("/"))
				.andExpect(status().isUnauthorized());
	}

}
