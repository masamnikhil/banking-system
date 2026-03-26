package com.bankingsys.auth_service.controller;

import com.bankingsys.auth_service.dto.LoginRequest;
import com.bankingsys.auth_service.dto.RegisterRequest;
import com.bankingsys.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody RegisterRequest request){

        Map<String, String> response = userService.register(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody LoginRequest loginRequest){
        Map<String, String> response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
