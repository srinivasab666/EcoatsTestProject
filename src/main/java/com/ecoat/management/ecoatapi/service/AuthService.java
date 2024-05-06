package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.AuthenticationResponse;
import com.ecoat.management.ecoatapi.dto.LoginRequestDTO;
import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.exception.UserAlreadyExistsException;
import com.ecoat.management.ecoatapi.model.User;

@Service
public interface AuthService {
    User registerUser(RegistrationDTO registrationDTO,MultipartFile orgLogo) throws UserAlreadyExistsException;

	AuthenticationResponse signin(LoginRequestDTO loginRequest);

	void verifyAccount(String token);

	Boolean verifyToken(String token);

	void resetPassword(String token, String password);

	User getUserDetails(String userId) throws UserNotFoundException;

	User getUserDetailsByEmail(String email) throws UserNotFoundException;

}
