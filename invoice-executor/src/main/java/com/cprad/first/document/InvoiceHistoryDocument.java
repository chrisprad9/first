package com.cprad.first.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "invoice_history")
public class InvoiceHistoryDocument {
    @Id
    private String id; // UUID

    private Long partnerId;
    private String status;
    private String description;
    private LocalDateTime executedAt;
}
