package com.ecoat.management.ecoatapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.model.User;
import com.ecoat.management.ecoatapi.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class HomeController {

	private final AuthService authService;

	@GetMapping("/home/userId/{userId}")
	public ResponseEntity<User> getUserDetailsV2(@PathVariable("userId") String userId) {
		try{
			return new ResponseEntity<>(authService.getUserDetails(userId), HttpStatus.OK);
		}catch (UserNotFoundException e){
			return new ResponseEntity<>(new User(),HttpStatus.CONFLICT);
		}
	}
	
}
