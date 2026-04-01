package com.bankingsys.account_service.dto;

import com.bankingsys.account_service.entity.TransactionStatus;
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
public class MetaDataRequest {

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String userId;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime transactionTime;
}
