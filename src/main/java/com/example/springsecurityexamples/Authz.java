package com.example.springsecurityexamples;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Authz {

	public boolean check(Authentication authentication, String id, String permission) {
		return "admin".equals(authentication.getName());
	}

}
