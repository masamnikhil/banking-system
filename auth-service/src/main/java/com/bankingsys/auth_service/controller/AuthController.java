package com.bankingsys.auth_service.controller;

import com.bankingsys.auth_service.dto.RegisterRequest;
import com.bankingsys.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody RegisterRequest request){

        Map<String, String> response = userService.register(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
