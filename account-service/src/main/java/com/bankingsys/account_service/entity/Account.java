package com.bankingsys.account_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends Auditable{

    private UUID id;
    private String accountNumber;
    private UUID customerId;
    private UUID userId;
    private AccountType accountType;
    private double balance;
    private Status accountStatus;

}
