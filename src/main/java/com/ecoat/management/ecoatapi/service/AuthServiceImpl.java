package com.ecoat.management.ecoatapi.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.AuthenticationResponse;
import com.ecoat.management.ecoatapi.dto.LoginRequestDTO;
import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.enums.RolesEnum;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.InvalidTokenException;
import com.ecoat.management.ecoatapi.exception.UserAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.mapper.CorporateMapper;
import com.ecoat.management.ecoatapi.mapper.EmployeeMapper;
import com.ecoat.management.ecoatapi.mapper.UserMapper;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateAddress;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.Role;
//import com.ecoat.management.ecoatapi.model.EmployeePhoneDetails;
import com.ecoat.management.ecoatapi.model.User;
import com.ecoat.management.ecoatapi.model.VerificationToken;
import com.ecoat.management.ecoatapi.repository.CorporateAddressRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.EmployeeRepository;
import com.ecoat.management.ecoatapi.repository.RolesRepository;
import com.ecoat.management.ecoatapi.repository.UserRepository;
import com.ecoat.management.ecoatapi.repository.VerificationTokenRepository;
import com.ecoat.management.ecoatapi.security.JwtProvider;
import com.ecoat.management.ecoatapi.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final CorporateRepository corpRepo;
	private final UserRepository userRepo;
	private final EmployeeRepository empRepo;
	private final CorporateAddressRepository corpAddressRepo;
	 private final VerificationTokenRepository verificationTokenRepo;
	 private final RolesRepository rolesRepo;

	private final CorporateMapper corporateMapper;
	private final EmployeeMapper employeeMapper;
	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authManager;
	private final JwtProvider jwtProvider;

	@Override
	@Transactional
	public User registerUser(RegistrationDTO regDTO,MultipartFile orgLogo) throws UserAlreadyExistsException {
		String method = "AuthServiceImpl.registerUser ";
		log.info(method + "Enter");
		User user = null;
		Employee employee = null;
		Employee manager = null;
		Corporate corporate = null;
		Set<Role> roleSet = new HashSet<>();
		List<CorporateAddress> corpAddresses =new ArrayList<>();
		try {
			Optional<Corporate> existingCorp = corpRepo.findByCorporateName(regDTO.getOrgName());
			Optional<Role> existingRole = rolesRepo.findByRoleName(RolesEnum.ADMIN);
			roleSet.add(existingRole.get());
			Optional<Employee> existingEmp = empRepo.findByEmail(regDTO.getUserEmail());
			if (existingCorp.isPresent()) {
				if (existingEmp.isPresent()) {
					throw new UserAlreadyExistsException();
				} else {
					employee = employeeMapper.mapRegDtoToEmployee(existingCorp.get(), regDTO);
					employee.setIs_Poc(1);
					if (null != regDTO.getSupervisorId()) {
						Optional<Employee> managerOptional = empRepo.findById(regDTO.getSupervisorId());
						if (managerOptional.isPresent()) {
							manager = managerOptional.get();
						}
					}
					employee.setManager(manager);
					user = userMapper.mapRegDtoToUser(existingCorp.get(), employee, regDTO);
					user.setPassword(passwordEncoder.encode(regDTO.getPassword()));
					user.setRoles(roleSet);
					user.setEffDate(new Date());
					//user.setExpDate(new Date());
					user = userRepo.save(user);
				}
			} else {
				if (existingEmp.isPresent()) {
					throw new UserAlreadyExistsException();
				}else{
					regDTO.setOrgCode(CommonUtil.generateCode(regDTO.getOrgName()));
					corporate = corporateMapper.mapRegDtoToCorporate(regDTO);
					CorporateAddress corpAddress = corporateMapper.mapRegDtoToCorpAddress(corporate, regDTO);
					corpAddresses.add(corpAddress);
					if(orgLogo != null) {
						corporate.setCorporateLogo(orgLogo.getBytes());
					}
					corporate.setCorpAddresses(corpAddresses);
					employee = employeeMapper.mapRegDtoToEmployee(corporate, regDTO);
					employee.setIs_Poc(1);
					if (null != regDTO.getSupervisorId()) {
						Optional<Employee> managerOptional = empRepo.findById(regDTO.getSupervisorId());
						if (managerOptional.isPresent()) {
							manager = managerOptional.get();
						}
					}
					employee.setManager(manager);
					user = userMapper.mapRegDtoToUser(corporate, employee, regDTO);
					user.setEffDate(new Date());
					user.setPassword(passwordEncoder.encode(regDTO.getPassword()));
					user.setRoles(roleSet);
					//corpRepo.save(corporate);
					user = userRepo.save(user);
				}
			}
		} catch (UserAlreadyExistsException e) {
			throw new UserAlreadyExistsException();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info(method + "Exit");
		return user;
	}

	@Override
	public AuthenticationResponse signin(LoginRequestDTO loginRequest) {
		String method = "AuthServiceImpl.signin ";
		log.info(method + "Enter");
		String token;
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			token = jwtProvider.generateToken(authentication);
			Set<String> roleSet =  authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
			log.info(method + "Exit");
		return AuthenticationResponse.builder().authenticationToken(token)	
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.roles(roleSet).build();
	}
	
	@Override
	public void verifyAccount(String token) {
		String method = "AuthServiceImpl.verifyAccount ";
		log.info(method + "Enter");
		Optional<VerificationToken> verificationToken = verificationTokenRepo.findByToken(token);
		if(verificationToken.isPresent()){
			if (verificationToken.get().getExpDate().before(new Date())) {
				throw new InvalidTokenException("Token got expired");
			}else {
				fetchUserAndEnable(verificationToken.get());
				log.info(method + "Exit");
			}
		}else{
			throw new InvalidTokenException("Invalid Token");
		}
	}

	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String method = "AuthServiceImpl.fetchUserAndEnable ";
		log.info(method + "Enter");
		String username = verificationToken.getUser().getUserId();
		User user = userRepo.findByUserId(username)
				.orElseThrow(() -> new EcoatsException("User not found with name - " + username));
		user.setIsActive(1);
		userRepo.save(user);
		log.info(method + "Exit");
	}
	
	@Override
	public Boolean verifyToken(String token) {
		String method = "AuthServiceImpl.verifyToken ";
		log.info(method + "Enter");
		Boolean isTokenValidated = false;
		Optional<VerificationToken> verificationToken = verificationTokenRepo.findByToken(token);
		if(verificationToken.isPresent()){
			if (verificationToken.get().getExpDate().before(new Date())) {
				throw new InvalidTokenException("Token got expired");
			}else {
				isTokenValidated = true;
			}
		}else{
			throw new InvalidTokenException("Invalid Token");
		}
		log.info(method + "Exit");
		return isTokenValidated;
	}

	@Override
	public void resetPassword(String userName, String password) {
		String method = "AuthServiceImpl.resetPassword ";
		log.info(method + "Enter");
		User user = userRepo.findByUserId(userName)
				.orElseThrow(() -> new EcoatsException("User not found with name - " + userName));
		user.setUpdatedOn(new Date());
		user.setPassword(passwordEncoder.encode(password));
		userRepo.saveAndFlush(user);
		log.info(method + "Exit");
	}

	@Override
	public User getUserDetails(String userId) throws UserNotFoundException {
		String method = "AuthServiceImpl.getUserDetails ";
		log.info(method + "Enter");
		Optional<User> user = userRepo.findByUserId(userId);
		if(user.isPresent()){
			log.info(method + "Exit");
			return user.get();
		}else{
			throw new UserNotFoundException();
		}
	}
	
	@Override
	public User getUserDetailsByEmail(String email) throws UserNotFoundException {
		String method = "AuthServiceImpl.getUserDetails ";
		log.info(method + "Enter");
		Optional<User> user = userRepo.findActiveUserByEmail(email);
		if(user.isPresent()){
			log.info(method + "Exit");
			return user.get();
		}else{
			throw new UserNotFoundException();
		}
	}
	
}
