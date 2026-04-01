package com.bankingsys.account_service.dto;

import com.bankingsys.account_service.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionEvent {

    private String transactionId;
    private String transactionType;
    private String fromAccount;   // null for deposit
    private String toAccount;     // null for withdrawal
    private BigDecimal amount;
    private String userId;
    private LocalDateTime transactionTime;
    private String status;
}
