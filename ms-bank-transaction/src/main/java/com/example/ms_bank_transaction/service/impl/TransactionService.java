package com.example.ms_bank_transaction.service.impl;

import com.example.ms_bank_transaction.model.BankAccount;
import com.example.ms_bank_transaction.model.CreditProduct;
import com.example.ms_bank_transaction.model.Transaction;
import com.example.ms_bank_transaction.repository.TransactionRepository;
import com.example.ms_bank_transaction.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService  implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> getTransactionByProductId(String productId) {
        return transactionRepository.getTransactionByProductId(productId)
                .switchIfEmpty(Flux.error(new RuntimeException("No se encontró transacciones para el producto : " + productId)));
    }

    @Override
    public Mono<Transaction> handleBankAccountTransaction(Transaction transaction) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/v1/bank-account/{id}", transaction.getProductId())
                .retrieve()
                .bodyToMono(BankAccount.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta no encontrada")))
                .flatMap(account -> {

                    BigDecimal newBalance;
                    transaction.setProductType(account.getAccountType());

                    switch (transaction.getTransactionType()) {
                        case DEPOSITO:
                            newBalance = account.getBalance().add(transaction.getAmount());
                            break;
                        case RETIRO:
                            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                                return Mono.error(new RuntimeException("Fondos insuficientes"));
                            }
                            newBalance = account.getBalance().subtract(transaction.getAmount());
                            break;
                        default:
                            return Mono.error(new RuntimeException("Tipo de transacción no válido"));
                    }
                    account.setBalance(newBalance);

                    Map<String,BigDecimal> balanceUpdate = new HashMap<>();
                    balanceUpdate.put("balance",newBalance);

                    return webClientBuilder.build()
                            .patch()
                            .uri("http://localhost:8082/api/v1/bank-account/balance/{id}", account.getId())
                            .bodyValue(balanceUpdate)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> handleCreditProductTransaction(Transaction transaction) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/v1/credit-product/{id}", transaction.getProductId())
                .retrieve()
                .bodyToMono(CreditProduct.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto de crédito no encontrado")))
                .flatMap(creditProduct -> {
                    BigDecimal newCreditBalance;
                    transaction.setProductType(creditProduct.getCreditType());

                    switch (transaction.getTransactionType()) {
                        case PAGO:
                            newCreditBalance = creditProduct.getCreditBalance().subtract(transaction.getAmount());
                            break;
                        case CONSUMO:
                            newCreditBalance = creditProduct.getCreditBalance().add(transaction.getAmount());
                            break;
                        default:
                            return Mono.error(new RuntimeException("Tipo de transacción no válido"));
                    }
                    creditProduct.setCreditBalance(newCreditBalance);

                    Map<String,BigDecimal> balanceUpdate = new HashMap<>();
                    balanceUpdate.put("creditBalance",newCreditBalance);

                    return webClientBuilder.build()
                            .patch()
                            .uri("http://localhost:8083/api/v1/credit-product/balance/{id}", creditProduct.getId())
                            .bodyValue(balanceUpdate)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .then(transactionRepository.save(transaction));
                });

    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());

        return switch (transaction.getProductType()) {
            case AHORRO, CORRIENTE -> handleBankAccountTransaction(transaction);
            case EMPRESARIAL, PERSONAL, TARJETACREDITO -> handleCreditProductTransaction(transaction);
            default -> Mono.error(new RuntimeException("Tipo de producto no válido"));
        };

    }

    @Override
    public Mono<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Transacción no encontrada")));
    }

    @Override
    public Mono<Transaction> updateTransaction(String id, Transaction transaction) {
        return null;
    }

    @Override
    public Mono<Void> deleteTransactionById(String id) {
        return null;
    }
}
