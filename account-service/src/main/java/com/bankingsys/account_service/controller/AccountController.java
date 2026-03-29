package com.bankingsys.account_service.controller;

import com.bankingsys.account_service.dto.AccountRequest;
import com.bankingsys.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    public ResponseEntity<String> createAccount(@RequestBody AccountRequest request){
          if()
    }
}
