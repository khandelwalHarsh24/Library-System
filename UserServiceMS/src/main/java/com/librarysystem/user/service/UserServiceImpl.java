package com.librarysystem.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.librarysystem.user.dto.AuthRequest;
import com.librarysystem.user.dto.AuthResponse;
import com.librarysystem.user.dto.UserDTO;
import com.librarysystem.user.entity.Role;
import com.librarysystem.user.entity.User;
import com.librarysystem.user.exception.UserServiceException;
import com.librarysystem.user.repository.UserRepository;

@Service(value="userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final Logger LOGGER = LogManager.getLogger(UserService.class);
	
	

	@Override
	public Long registerUser(UserDTO userDto) throws UserServiceException {
		// TODO Auto-generated method stub
		User userDetail=userRepository.findByUserNameAndEmail(userDto.getUserName(), userDto.getEmail());
		if(userDetail!=null) {
			throw new UserServiceException("User alreaydy exist");
		}
		if(userDto.getRole()==null) {
			userDto.setRole(Role.USER);
		}
		if(userDto.getCreatedAt()==null) {
			userDto.setCreatedAt(LocalDateTime.now());
		}
		userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		LOGGER.info("User Register Successfully");
		return userRepository.save(modelMapper.map(userDto, User.class)).getUserId();
	}


	@Override
	public AuthResponse loginUser(String email, String password) throws UserServiceException {
		User user = userRepository.findByEmail(email);
		if(user==null) throw new UserServiceException("User does not exist");
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserServiceException("Password is Incorrect");
		}
		AuthRequest authData=new AuthRequest(email,password,user.getRole().toString());
		AuthResponse data = webClient.post()
                .uri("http://localhost:2000/api/v1/auth/generate-token")
                .bodyValue(authData)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block(); 
		LOGGER.info("User Login Successfully");
		return data;
	}


	@Override
	public List<UserDTO> getAllUsers() throws UserServiceException {
		Iterable<User> userData=userRepository.findAll();
		List<User> userList = StreamSupport.stream(userData.spliterator(), false)
                .collect(Collectors.toList());
		List<UserDTO> userDTOList = userList.stream()
			    .map(user -> modelMapper.map(user, UserDTO.class))
			    .collect(Collectors.toList());
		if(userDTOList==null) throw new UserServiceException("Not Exist Any Users");
		LOGGER.info("Successfully Retrieve data");
		return userDTOList;
	}
	
	
	@Override
	public void updateRole(Long id,String role) throws UserServiceException {
		// TODO Auto-generated method stub
		User user= userRepository.findById(id).orElseThrow(()->new UserServiceException("User does not exist"));
		Role newRole=(role=="USER")? Role.USER: Role.ADMIN;
		user.setRole(newRole);
		LOGGER.info("Update User Successfully");
		userRepository.save(user);
	}


	@Override
	public UserDTO userProfile() throws UserServiceException {
		String email=SecurityContextHolder.getContext().getAuthentication().getName();
		User userData=userRepository.findByEmail(email);
		LOGGER.info("User Profile Retrieved Successfully");
		return modelMapper.map(userData, UserDTO.class);
	}
	
//	public
}
