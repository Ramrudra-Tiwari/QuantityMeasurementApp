package com.app.quantitymeasurementapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.dto.AuthDtoRequest;
import com.app.quantitymeasurementapp.model.User;
import com.app.quantitymeasurementapp.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public String signup(@RequestBody User user) {
		return authService.signup(user);
	}
	
	@PostMapping("/signin")
	public String signin(@RequestBody AuthDtoRequest authDtoRequest) {
		return authService.signin(authDtoRequest);
	}
}
