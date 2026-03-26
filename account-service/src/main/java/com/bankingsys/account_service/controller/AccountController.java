package com.bankingsys.account_service.controller;

import com.bankingsys.account_service.dto.AccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    public ResponseEntity<String> createAccount(@RequestBody AccountRequest request){
          return null;
    }
}
