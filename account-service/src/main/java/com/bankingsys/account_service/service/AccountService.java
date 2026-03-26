package com.bankingsys.account_service.service;

import com.bankingsys.account_service.dto.AccountRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    String createAccount(AccountRequest request);
}
