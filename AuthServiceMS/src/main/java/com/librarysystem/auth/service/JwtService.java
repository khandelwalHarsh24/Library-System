package com.librarysystem.auth.service;

import java.security.Key;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private String secret="Oz4KZCRFq5+fR2ZMh2i0G7o8SvM7UyaQ3VLO8zUSe6I=";
	private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
	
	private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
	
	 public String generateToken(String email,String role) {
	        return Jwts.builder()
	                .setSubject(email)
	                .claim("role", role)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	                .compact();
	 }
	 
	 public String extractRole(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .get("role",String.class);
	 }
	 
	 public String extractEmail(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	 }
	 
//	 public Long extractId(String token) {
//		 return Jwts.parserBuilder()
//	                .setSigningKey(getSigningKey())
//	                .build()
//	                .parseClaimsJws(token)
//	                .getBody().get("userId",Long.class);
//	 }
	 
	 public boolean isTokenValid(String token) {
	        try {
	            Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token);
	            return true;
	        } catch (JwtException e) {
	            return false;
	        }
	 }
}
