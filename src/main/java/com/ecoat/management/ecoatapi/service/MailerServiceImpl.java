package com.ecoat.management.ecoatapi.service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.User;
import com.ecoat.management.ecoatapi.model.VerificationToken;
import com.ecoat.management.ecoatapi.repository.EmployeeRepository;
import com.ecoat.management.ecoatapi.repository.UserRepository;
import com.ecoat.management.ecoatapi.repository.VerificationTokenRepository;
import com.ecoat.management.ecoatapi.util.CommonUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Import(SimpleJavaMailSpringSupport.class)
public class MailerServiceImpl implements MailerService{

	@Autowired  private Mailer mailer;
	@Autowired Configuration configuration;
	@Autowired private VerificationTokenRepository verificationTokenRepository;
	@Autowired private UserRepository userRepo;
	@Autowired  private EmployeeRepository empRepo;
	@Autowired  private PasswordEncoder passwordEncoder;
    @Value("${validationtoken.expiration.time}")
    private Long validationTokenExpInMillis;
    
    @Value("${ecoats.contextpath}")
    private String contextPath;
    
    @Value("${ecoats.fromMail}")
    private String fromMail;
    
    
    @Override
    public void sendVerificationMail(String emailAddr,String firstname,User user){

        try{
            Email email = EmailBuilder.startingBlank()
                    .from("testmailer",fromMail)
                    .to(emailAddr)
                    .withSubject("Please confirm your email")
                    .withHTMLText(getEmailContent(firstname,user))
                    .buildEmail();

           mailer.sendMail(email);
        }catch(Exception e){
            log.error("exception occured in sendVerificationMail"+e.getMessage());
        }

    }
    
    @Override
    public void resendVerificationEmail(String userId) {
		User user = userRepo.findByUserId(userId)
				.orElseThrow(() -> new EcoatsException("User not found with name - " + userId));
		if (user.getEmployee() != null) {
			sendVerificationMail(user.getEmployee().getEmail(),user.getEmployee().getFirstName(),user);
		}
	}
    
    private String getEmailContent(String firstname,User user) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("token", generateVerificationToken(user));
        model.put("firstname", firstname.toUpperCase());
        model.put("contextPath", contextPath);
        configuration.getTemplate("verification.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        log.info("token:  "+token);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpDate(Date.from(Instant.now().plusMillis(validationTokenExpInMillis)));
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    
    @Override
	public void sendForgotPasswordLink(String email) throws UserNotFoundException {
		Optional<Employee> existingEmp = empRepo.findByEmail(email);
		String randomPass = CommonUtil.alphaNumericString(10);
		if (existingEmp.isPresent()) {
			Employee employee = existingEmp.get();
			User user = userRepo.findByEmployeeId(employee.getEmpId());
			user.setPassword(passwordEncoder.encode(randomPass));
			userRepo.saveAndFlush(user);
			//String token = generateVerificationToken(user);
			sendResetPasswordLink(email,user,randomPass);
		} else {
			throw new UserNotFoundException();
		}
	}
    
    private void sendResetPasswordLink(String emailAddress,User user,String tempPassword) {
    	String url = contextPath + "auth/tokenValidation?token="; //+ //token;
    	 Email email = EmailBuilder.startingBlank()
                 .from("testmailer",fromMail)
                 .to(emailAddress)
                 .withSubject("Ecoats Reset password")
                 .withPlainText("Please Use the UserId  "+user.getUserId()+"\r\n"+"Please Use the temporary password  "
                 		+  tempPassword+ "\r\n")
                 //.appendText(url)
                 .buildEmail();

        mailer.sendMail(email);
    }
    
}
