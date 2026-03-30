package com.app.quantitymeasurementapp.security;

import java.util.Date;

import javax.crypto.SecretKey;


import org.springframework.stereotype.Component;

import com.app.quantitymeasurementapp.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "sdfghjxcvbghjklhjkogbhjkl4412125121211515351gbvhbjndjanma";
	
	public String generateToken(User user) {
		return Jwts.builder()
				.subject(user.getUsername())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(getKey())
				.compact();
	}
	
	 public String extractUsername(String token) {
	        return getClaims(token).getSubject();
	    }
	    public Claims getClaims(String token){
	        return Jwts.parser()
	                .verifyWith(getKey())
	                .build().parseSignedClaims(token)
	                .getPayload();
	    }
	    public SecretKey getKey(){
	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	    }
}
