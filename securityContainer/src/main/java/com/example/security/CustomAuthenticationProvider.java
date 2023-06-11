package com.example.security;

import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // fetch the employee from the database by username
        String tokenUsername = authentication.getName();
        String tokenPassword = authentication.getCredentials().toString();
        Optional<Employee> employee = employeeRepository.findByUsername(tokenUsername); // todo username-mail

        // if there exists an employee with the given username
        if(employee.isPresent()) {

            if(passwordEncoder.matches(tokenPassword, employee.get().getPassword())) {

                // fetch the granted authorities from the database and create a username-password-authentication token using
                // that employee's username, password and authorities
                Set<Roles> roles = employee.get().getRoles();
                Set<GrantedAuthority> grantedAuthoritiesSet = customUserDetailsService.getSimpleGrantedAuthorities(roles);

                // last parameter is Set<GrantedAuthorities> type
                return new UsernamePasswordAuthenticationToken(tokenUsername, tokenPassword, grantedAuthoritiesSet);
            }

            else {
                throw new BadCredentialsException("Invalid password");
            }
        }

        else {
            throw new BadCredentialsException("No user registration with this username");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}