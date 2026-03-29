package com.bankingsys.account_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@Builder
public class Account extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, updatable = false)
    private String accountNumber;
    @Column(nullable = false, updatable = false, unique = true, length = 11)
    private UUID userId;
    private String accountType;
    private BigDecimal balance;
    private String accountStatus;
    @Column(nullable = false, updatable = false)
    private UUID branchId;

    @PrePersist
    public void prePersist() {
        if (accountStatus == null) {
            accountStatus = Status.ACTIVE.toString();
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }

}
