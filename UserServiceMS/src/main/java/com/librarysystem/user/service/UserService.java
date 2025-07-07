package com.librarysystem.user.service;

import java.util.List;

import com.librarysystem.user.dto.AuthResponse;
import com.librarysystem.user.dto.UserDTO;
import com.librarysystem.user.exception.UserServiceException;

public interface UserService {
	public Long registerUser(UserDTO user) throws UserServiceException;
	public AuthResponse loginUser(String email,String password) throws UserServiceException;
	public List<UserDTO> getAllUsers() throws  UserServiceException;
	public void updateRole(Long id,String role) throws UserServiceException;
	public UserDTO userProfile() throws UserServiceException;
}
