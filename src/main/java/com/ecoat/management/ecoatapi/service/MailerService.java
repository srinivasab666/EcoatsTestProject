package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.model.User;

public interface MailerService {
    void sendVerificationMail(String emailAddr,String firstname, User user);

	void resendVerificationEmail(String userId);

	void sendForgotPasswordLink(String email) throws UserNotFoundException;
}
