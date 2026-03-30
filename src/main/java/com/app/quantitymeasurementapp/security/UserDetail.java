package com.app.quantitymeasurementapp.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurementapp.model.User;
import com.app.quantitymeasurementapp.repository.SqlDatabase;

@Service
public class UserDetail implements UserDetailsService {
	
	@Autowired
	private SqlDatabase sqlDatabase;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = sqlDatabase.findByEmail(username);
		
		return org.springframework.security.core.userdetails.User
				.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.build();
	}

}
