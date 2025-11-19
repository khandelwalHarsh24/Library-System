package com.librarysystem.auth.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.auth.dto.AuthRequest;
import com.librarysystem.auth.dto.AuthResponse;
import com.librarysystem.auth.exception.AuthException;
import com.librarysystem.auth.service.JwtService;
import com.librarysystem.auth.utility.LoggingAspect;

@RestController
@RequestMapping(value="/api/v1/auth")
public class AuthController {

	@Autowired
	private JwtService jwtService;
	
	public static final Logger LOGGER = 
			LogManager.getLogger(AuthController.class); 
	
	@PostMapping("/generate-token")
	public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest request) throws AuthException{
		String token = jwtService.generateToken(request.getEmail(), request.getRole());
		return new ResponseEntity<AuthResponse>(new AuthResponse(token,request.getRole()),HttpStatus.OK);
	}
	
	
	@GetMapping("/validate-token")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) throws AuthException{
		token = token.replace("Bearer ", "");
        String role = jwtService.extractRole(token);
        String email= jwtService.extractEmail(token);
		return new ResponseEntity<String>(role+","+email,HttpStatus.OK);
	}
}
