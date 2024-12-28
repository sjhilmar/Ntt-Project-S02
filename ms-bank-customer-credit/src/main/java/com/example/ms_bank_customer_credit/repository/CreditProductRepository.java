package com.example.ms_bank_customer_credit.repository;

import com.example.ms_bank_customer_credit.model.CreditProduct;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditProductRepository extends ReactiveMongoRepository<CreditProduct,String> {

    Flux<CreditProduct> findCreditProductByCustomerId(String customerId);

}
