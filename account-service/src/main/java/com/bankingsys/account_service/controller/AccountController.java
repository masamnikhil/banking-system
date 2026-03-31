package com.bankingsys.account_service.controller;

import com.bankingsys.account_service.dto.AccountRequest;
import com.bankingsys.account_service.dto.DepositRequest;
import com.bankingsys.account_service.dto.TransferRequest;
import com.bankingsys.account_service.dto.WithdrawRequest;
import com.bankingsys.account_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountRequest request){
          if(accountService.createAccount(request))
              return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);

          return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawAmount(@Valid @RequestBody WithdrawRequest request, @RequestHeader("Authorization") String token){

        if(accountService.withDrawAmount(request, token))
        return ResponseEntity.ok("Withdraw successful");

        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositAmount(@Valid @RequestBody DepositRequest request, @RequestHeader("Authorization") String token){

        if(accountService.depositAmount(request, token))
            return ResponseEntity.ok("Deposit successful");
        else
        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferAmount(@Valid @RequestBody TransferRequest request, @RequestHeader("Authorization") String token){

        if(accountService.transferAmount(request, token))
            return ResponseEntity.ok("transfer Successful");
            else
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
