package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?>registerUser(@RequestBody UserDTO userDTO){
		try {
			UserEntity user = UserEntity.builder()
										.email(userDTO.getEmail())
										.username(userDTO.getUsername())
										.password(passwordEncoder.encode(userDTO.getPassword()))
										.build();
			
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = userDTO.builder()
										.email(registeredUser.getEmail())
										.id(registeredUser.getId())
										.username(registeredUser.getUsername())
										.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		}catch(Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getUsername(),userDTO.getPassword(),passwordEncoder);
		
		if(user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
													.email(user.getEmail())
													.id(user.getId())
													.username(user.getUsername())//여기수정
													.token(token)
													.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder()
											.error("Login failed")
											.build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/userinfoset")
	public void userInfoSet(@RequestBody UserDTO userDTO) {
		//user 원래 정보
		String before_userset = userService.getUserEntity(userDTO.getEmail().toString()).getId();
		
		//업데이트 정보 & 기존 정보를 담을 임시 개체
		UserEntity after_userset = UserEntity.builder().id(before_userset)
												.email(userDTO.getEmail())
												.username(userDTO.getUsername())
												.password(passwordEncoder.encode(userDTO.getPassword()))
												.build();
		//업데이트
		userService.updateUserEntity(after_userset);
	}
}
