package com.example.ms_bank_customer_credit.controller;

import com.example.ms_bank_customer_credit.model.BalanceUpdateRequest;
import com.example.ms_bank_customer_credit.model.CreditProduct;
import com.example.ms_bank_customer_credit.service.ICreditProductService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/credit-product")
public class CreditProductController {

     @Autowired
    private ICreditProductService creditProductService;

     @GetMapping
    public Flux<CreditProduct> getAllCreditProducts() {
        log.info("Consultando todos los creditos");
        return creditProductService.getAllCreditProducts();
    }

    @GetMapping("/{id}")
    public Mono<CreditProduct> getCreditProductById(@PathVariable String id) {
         log.info("Consultando credito con id {}", id);
         return creditProductService.getCreditProductById(id)
                 .doOnError(e -> log.error("Error al consultar el credito con id: {}", id, e))
                 .onErrorResume(e -> Mono.error(new RuntimeException("Error al consultar el credito con id: " + id)));
    }

    @GetMapping("/customer/{customerId}")
    public Flux<CreditProduct> getCreditProductsByCustomerId(@PathVariable String customerId) {
        log.info("Consultando creditos del cliente con CustomerId {}", customerId);
        return creditProductService.findCreditProductByCustomerId(customerId)
                .doOnError(e -> log.error("Error al consultar los creditos del cliente con id: {}", customerId, e))
                .onErrorResume(e -> Flux.error(new RuntimeException("Error al consultar los creditos del cliente con id: " + customerId)));
    }

    @PostMapping
    public Mono<ResponseEntity<CreditProduct>> createCreditProduct(@Valid @RequestBody CreditProduct creditProduct) {
         log.info("Creando credito {}", creditProduct);
         return creditProductService.createCreditProduct(creditProduct)
                 .map(createdCreditProduct -> ResponseEntity.status(HttpStatus.CREATED).body(createdCreditProduct))
                    .doOnError(e -> log.error("Error al crear el credito {}", creditProduct, e))
                 .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()));

    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreditProduct>> updateCreditProduct(@PathVariable String id, @Valid @RequestBody CreditProduct creditProduct) {
        log.info("Actualizando credito {}", creditProduct);
        return creditProductService.updateCreditProduct(id, creditProduct)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("Error al actualizar el credito con id: {}", id, e))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PatchMapping("/balance/{id}")
    public Mono<ResponseEntity<CreditProduct>> updateBalance(@PathVariable String id , @Valid @RequestBody BalanceUpdateRequest balanceUpdateRequest) {
        log.info("Actualizando balance de credito con id {}", id);

        return creditProductService.updateCreditBalance(id, balanceUpdateRequest.getCreditBalance())
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("Error al actualizar el balance del credito con id: {}", id, e))
                .onErrorResume(RuntimeException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCreditProductById(@PathVariable String id) {
         log.info("Eliminando credito con id {}", id);
        return creditProductService.deleteCreditProductById(id)
                .doOnError(e -> log.error("Error al eliminar el credito con id: {}", id, e))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al eliminar el credito con id: " + id)));
    }

}