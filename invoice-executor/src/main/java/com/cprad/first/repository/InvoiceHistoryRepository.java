package com.cprad.first.repository;

import com.cprad.first.document.InvoiceHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceHistoryRepository extends MongoRepository<InvoiceHistoryDocument, String> {
}
