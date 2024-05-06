package com.ecoat.management.ecoatapi.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.AuthenticationResponse;
import com.ecoat.management.ecoatapi.dto.LoginRequestDTO;
import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.InvalidTokenException;
import com.ecoat.management.ecoatapi.exception.UserAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.model.User;
import com.ecoat.management.ecoatapi.service.AuthService;
import com.ecoat.management.ecoatapi.service.MailerService;
import com.ecoat.management.ecoatapi.util.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final MailerService mailerService;

	@GetMapping("/health")
	public ResponseEntity<String> home() {
		return new ResponseEntity<>("{homepage:true}", HttpStatus.OK);
	}
	
	@PostMapping(value = "/signup")
	public ResponseEntity<String> signup(@RequestParam(name = "logo", required=false) MultipartFile orgLogo, @Valid @RequestParam String regiDTO){
		String method = "AuthController.signup ";
		log.info(method + "Enter");
		try {
			if(!StringUtils.hasLength(regiDTO)) {
				return new ResponseEntity<>("regDto should not be empty", HttpStatus.NOT_ACCEPTABLE);
			}
			ObjectMapper mapper = new ObjectMapper();
			RegistrationDTO registrationDTO;
				registrationDTO = mapper.readValue(regiDTO, RegistrationDTO.class);
				boolean isValid = ValidationUtil.isValid(registrationDTO.getOrgName(),registrationDTO.getOrgAddr1(),registrationDTO.getOrgCity(),registrationDTO.getOrgState(),
						registrationDTO.getOrgZip(),registrationDTO.getOrgCountry(),registrationDTO.getOrgPhone(),registrationDTO.getUserFirstName(),
						registrationDTO.getUserLastName(),registrationDTO.getUserEmail(),registrationDTO.getOrgPhone(),registrationDTO.getPassword());
			if(!isValid) {
				return new ResponseEntity<>("The required fields are missed", HttpStatus.BAD_REQUEST);
			}
			User user = authService.registerUser(registrationDTO,orgLogo);
			// send verification email
			mailerService.sendVerificationMail(registrationDTO.getUserEmail(),registrationDTO.getUserFirstName(),user);
			log.info(method + "Exit");
			return new ResponseEntity<>("Registration successful", HttpStatus.CREATED);
		} catch (UserAlreadyExistsException userAlreadyExistsException) {
			return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
		}  catch (JsonProcessingException e) {
			return new ResponseEntity<>("Not able to parse json string", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequestDTO loginRequest) {
		AuthenticationResponse authenticationResponse = null;
		String method = "AuthController.login ";
		log.info(method + "Enter");
		try {
			authenticationResponse = authService.signin(loginRequest);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
	} 

	@GetMapping("/emailconfirmation")
	public ResponseEntity<String> sendVerificationToken(@RequestParam("token") String token) {
		String method = "AuthController.sendVerificationToken ";
		log.info(method + "Enter");
		log.info("token:"+token);
		try {
			authService.verifyAccount(token);
		} catch (InvalidTokenException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		log.info(method + "Exit");
		return new ResponseEntity<>("verification sucessful", HttpStatus.OK);
	}
	
	@GetMapping("/resendEmailConfirmation")
	public ResponseEntity<String> resendVerificationToken(@RequestParam("userId") String userId) {
		String method = "AuthController.resendVerificationToken ";
		log.info(method + "Enter");
		try {
			mailerService.resendVerificationEmail(userId);
		} catch (EcoatsException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return new ResponseEntity<>("verification email sent sucessful", HttpStatus.OK);
	}
	
	@GetMapping("/forgotPassword")
	public ResponseEntity<String> sendForgotPasswordLink(@RequestParam("email") String email) throws UserNotFoundException {
		String method = "AuthController.sendForgotPasswordLink ";
		log.info(method + "Enter");
		log.info("The request email: " + email);
		try {
			authService.getUserDetailsByEmail(email);
			mailerService.sendForgotPasswordLink(email);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return new ResponseEntity<>("password reset email sent sucessful", HttpStatus.OK);
	}
	
	@GetMapping("/tokenValidation")
	public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
		ResponseEntity<String> response = null;
		String method = "AuthController.validateToken ";
		log.info(method + "Enter");
		try {
			Boolean isTokenValidated = authService.verifyToken(token);
			if (isTokenValidated) {
				response = new ResponseEntity<>("Token validated successfully", HttpStatus.OK);
			}
		} catch (InvalidTokenException e) {
			return new ResponseEntity<>("Token expired", HttpStatus.NOT_ACCEPTABLE);
		}
		log.info(method + "Exit");
		return response;
	}
	
	@GetMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@RequestParam("userId") String userId, @RequestParam("password") String password) {
		String method = "AuthController.resetPassword ";
		log.info(method + "Enter");
		try {
			authService.resetPassword(userId,password);
		} catch (EcoatsException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return new ResponseEntity<>("password reset sucessfully", HttpStatus.OK);
	}
}
