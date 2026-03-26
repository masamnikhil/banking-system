package com.bankingsys.account_service.dto;

import com.bankingsys.account_service.entity.AccountType;
import com.bankingsys.account_service.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    private String userId;
    private String accountType;
    private double balance;
    private String accountStatus;

}
