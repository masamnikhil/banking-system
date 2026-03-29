package com.bankingsys.auth_service.serviceImpl;

import com.bankingsys.auth_service.config.JwtUtil;
import com.bankingsys.auth_service.dto.CustomerRequestDto;
import com.bankingsys.auth_service.dto.LoginRequest;
import com.bankingsys.auth_service.dto.RegisterRequest;
import com.bankingsys.auth_service.entity.Role;
import com.bankingsys.auth_service.entity.User;
import com.bankingsys.auth_service.exception.CustomerServiceException;
import com.bankingsys.auth_service.exception.UserAuthenticationException;
import com.bankingsys.auth_service.feignclient.CustomerClient;
import com.bankingsys.auth_service.repository.UserRepository;
import com.bankingsys.auth_service.service.UserService;
import feign.FeignException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CustomerClient customerClient;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(RegisterRequest registerRequest) {
        boolean user = userRepository.existsByUsername(registerRequest.getUsername());

        if (user)
            throw new EntityExistsException("username already exists");

        User saveUser = User.builder().username(registerRequest.getUsername()).password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getEmail().endsWith("@bank.com") ? Role.ROLE_MANAGER : Role.ROLE_CUSTOMER).build();

        User savedUser = userRepository.saveAndFlush(saveUser);
        CustomerRequestDto userRequestDto = CustomerRequestDto.builder().userId(savedUser.getId()).email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber()).firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName()).dateOfBirth(registerRequest.getDateOfBirth()).build();
        final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
        logger.info(" savedUser id {}", savedUser.getId());
        try {
            ResponseEntity<HttpStatus> response = customerClient.createCustomer(userRequestDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                String accessToken = jwtUtil.generateToken(savedUser.getId().toString(), savedUser.getRole().toString());
                return Map.of("accessToken", accessToken, "userId", savedUser.getId().toString());
            }
        } catch (FeignException ex) {
            if (ex.status() == 500) {
                throw new CustomerServiceException("Invalid customer data");
            }
            if (ex.status() == 409) {
                throw new CustomerServiceException(ex.contentUTF8());
            }
        }
        return null;
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Optional<User> savedUser = userRepository.findByUsername(loginRequest.getUsername());
            String accessToken = jwtUtil.generateToken(savedUser.get().getId().toString(), savedUser.get().getRole().toString());
            return Map.of("accessToken", accessToken, "userId", savedUser.get().getId().toString());
        } catch (BadCredentialsException ex) {
            throw new UserAuthenticationException("incorrect password");
        } catch (AuthenticationException ex) {

            String message = "";

            if (ex instanceof LockedException) {
                message = "Account locked contact branch";
            } else if (ex instanceof DisabledException) {
                message = "Account disabled contact branch";
            }
            else{
                message = ex.getMessage();
            }
            throw new UserAuthenticationException(message);
        }
    }
}
