package com.cprad.first.consumer;

import com.cprad.first.document.InvoiceHistoryDocument;
import com.cprad.first.repository.InvoiceHistoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InvoiceConsumer {
    private final InvoiceHistoryRepository historyRepository;

    public InvoiceConsumer(InvoiceHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @KafkaListener(topics = "invoice-topic", groupId = "invoice-execution-group")
    public void listenInvoiceGeneration(String partnerId) {
        System.out.println("[CONSUMER] Consuming message from Kafka!");
        System.out.println("Assume message is processed for invoicing flow.");

        InvoiceHistoryDocument log = new InvoiceHistoryDocument();
        log.setPartnerId(Long.parseLong(partnerId));
        log.setStatus("SUCCESS");
        log.setDescription("Invoice PDF successfully generated via dynamic schedule calculation engine.");
        log.setExecutedAt(LocalDateTime.now());

        InvoiceHistoryDocument savedLog = historyRepository.save(log);
        System.out.println("[MONGO] Success writing audit trail with Document ID: " + savedLog.getId());
    }
}
