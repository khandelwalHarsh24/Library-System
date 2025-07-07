package com.librarysystem.auth.controller;


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
import com.librarysystem.auth.service.JwtService;

@RestController
@RequestMapping(value="/api/v1/auth")
public class AuthController {

	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/generate-token")
	public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest request){
		String token = jwtService.generateToken(request.getEmail(), request.getRole());
		return new ResponseEntity<AuthResponse>(new AuthResponse(token,request.getRole()),HttpStatus.OK);
	}
	
	
	@GetMapping("/validate-token")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token){
		token = token.replace("Bearer ", "");
		if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT");
        }
        String role = jwtService.extractRole(token);
        String email= jwtService.extractEmail(token);
		return new ResponseEntity<String>(role+","+email,HttpStatus.OK);
	}
}
