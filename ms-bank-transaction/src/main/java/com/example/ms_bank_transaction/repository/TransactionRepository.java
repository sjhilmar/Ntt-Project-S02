package com.example.ms_bank_transaction.repository;

import com.example.ms_bank_transaction.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> getTransactionByProductId(String productId);
}
