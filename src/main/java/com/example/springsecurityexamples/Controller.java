package com.example.springsecurityexamples;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/")
	@PreAuthorize("@authz.check(authentication, 'id', 'read')")
	public String check() {
		return "ok";
	}

}
