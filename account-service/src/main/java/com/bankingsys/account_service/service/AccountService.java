package com.bankingsys.account_service.service;

import com.bankingsys.account_service.dto.AccountRequest;
import com.bankingsys.account_service.dto.DepositRequest;
import com.bankingsys.account_service.dto.TransferRequest;
import com.bankingsys.account_service.dto.WithdrawRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    boolean createAccount(AccountRequest request);
    boolean withDrawAmount(WithdrawRequest request, String token);
    boolean depositAmount(DepositRequest request, String token);
    boolean transferAmount(TransferRequest request, String token);
}
