package com.example.ms_bank_transaction.service;

import com.example.ms_bank_transaction.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {
    Flux<Transaction> getAllTransactions();
    Flux<Transaction> getTransactionByProductId(String productId);
    Mono<Transaction> getTransactionById(String id);
    Mono<Transaction> handleBankAccountTransaction(Transaction transaction);
    Mono<Transaction> handleCreditProductTransaction(Transaction transaction);
    Mono<Transaction> createTransaction(Transaction transaction);
    Mono<Transaction> updateTransaction(String id, Transaction transaction);
    Mono<Void> deleteTransactionById(String id);
}
