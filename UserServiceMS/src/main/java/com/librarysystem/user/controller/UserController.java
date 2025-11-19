package com.librarysystem.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.user.dto.AuthResponse;
import com.librarysystem.user.dto.LoginRequest;
import com.librarysystem.user.dto.UserDTO;
import com.librarysystem.user.exception.UserServiceException;
import com.librarysystem.user.service.UserService;

@RestController
@RequestMapping(value="/api/v1/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(value="/register")
	public ResponseEntity<String> registerUser(@RequestBody UserDTO userData) throws UserServiceException {
		Long userId=userService.registerUser(userData);
		return new ResponseEntity<String>("User Register with Id : "+userId,HttpStatus.OK);
	}
	
	@PostMapping(value="/login")
	public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest logindata) throws UserServiceException{
		AuthResponse data=userService.loginUser(logindata.getEmail(),logindata.getPassword());
		return new ResponseEntity<AuthResponse>(data,HttpStatus.OK);
	}
	
	@GetMapping(value="/allusers")
	public ResponseEntity<List<UserDTO>> allUsers() throws UserServiceException{
		List<UserDTO> usersData=userService.getAllUsers();
		return new ResponseEntity<List<UserDTO>>(usersData,HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}/role")
	public ResponseEntity<String> updateRole(@PathVariable Long id,@RequestBody String role) throws UserServiceException{
		userService.updateRole(id, role);
		return new ResponseEntity<String>("Role has updated for user",HttpStatus.OK);
	}
	
	@GetMapping(value="/profile")
	public ResponseEntity<UserDTO> getProfile() throws UserServiceException{
		UserDTO userData=userService.userProfile();
		return new ResponseEntity<UserDTO>(userData,HttpStatus.OK);
	}
}
