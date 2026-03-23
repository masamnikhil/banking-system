package com.bankingsys.auth_service.service;

import com.bankingsys.auth_service.dto.LoginRequest;
import com.bankingsys.auth_service.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    Map<String, String> register(RegisterRequest registerRequest);
    String login(LoginRequest loginRequest);
}
