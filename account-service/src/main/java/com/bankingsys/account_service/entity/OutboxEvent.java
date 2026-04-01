package com.bankingsys.account_service.entity;

import com.bankingsys.account_service.dto.TransactionEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String topic;
    private String key;
    private TransactionEvent payload;
    private boolean sent;
}
