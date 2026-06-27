package com.cprad.first.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumer {
    @KafkaListener(topics = "invoice-topic", groupId = "invoice-execution-group")
    public void listenInvoiceGeneration(String partnerId) {
        System.out.println("------------------------------------------------------------");
        System.out.println("[CONSUMER] Consuming message from Kafka!");
        System.out.println("[CONSUMER] Processing execution invoice for Partner ID: " + partnerId);

        System.out.println("[CONSUMER] Triggering query generator...");
        System.out.println("[CONSUMER] STATUS: Success creating Invoice PDF for Partner ID " + partnerId);
        System.out.println("------------------------------------------------------------");
    }
}
