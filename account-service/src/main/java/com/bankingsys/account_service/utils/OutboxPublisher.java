package com.bankingsys.account_service.utils;

import com.bankingsys.account_service.dto.TransactionEvent;
import com.bankingsys.account_service.entity.OutboxEvent;
import com.bankingsys.account_service.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishOutbox() {

        List<OutboxEvent> events = outboxRepository.findBySentFalse();

        for (OutboxEvent event : events) {
            kafkaTemplate.send(
                    event.getTopic(),
                    event.getKey(),
                    event.getPayload()
            );

            event.setSent(true);
            outboxRepository.save(event);
        }
    }
}
