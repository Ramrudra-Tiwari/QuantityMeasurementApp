
package com.app.quantitymeasurementapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurementapp.dto.AuthDtoRequest;
import com.app.quantitymeasurementapp.model.User;
import com.app.quantitymeasurementapp.repository.SqlDatabase;
import com.app.quantitymeasurementapp.security.JwtUtil;

@Service
public class AuthService {
    
	@Autowired
	private SqlDatabase repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public String signup(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		repo.save(user);
		return "User registered with name "+user.getUsername();
	}
	
	public String signin(AuthDtoRequest authDtoRequest) {
		manager.authenticate(new UsernamePasswordAuthenticationToken(authDtoRequest.getEmail(), authDtoRequest.getPassword()));
		return jwtUtil.generateToken(repo.findByEmail(authDtoRequest.getEmail()));
	}
}
