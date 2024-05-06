package com.ecoat.management.ecoatapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.model.Role;
import com.ecoat.management.ecoatapi.model.User;
import com.ecoat.management.ecoatapi.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		 Optional<User> userOptional = userRepository.findByUserId(userId);
	        User user = userOptional
	                .orElseThrow(() -> new UsernameNotFoundException("No user " +
	                        "Found with username : " + userId));

	        return new org.springframework.security
	                .core.userdetails.User(user.getUserId(), user.getPassword(),
	                true, true, true,
	                true, getAuthorities(user));
	}

	public Collection<? extends GrantedAuthority> getAuthorities(User user) {
		
		Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleEnum().getRole()));
        }
        
        return authorities;
//        return singletonList(new SimpleGrantedAuthority(user.getUserId()));
    }
}
