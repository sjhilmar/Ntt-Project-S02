package com.example.ms_bank_customer_account.repository;
import com.example.ms_bank_customer_account.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Flux<BankAccount> findBankAccountByCustomerId(String customerId);

}
