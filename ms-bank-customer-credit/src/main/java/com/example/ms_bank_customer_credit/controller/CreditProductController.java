package com.example.ms_bank_customer_credit.controller;

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
         log.debug("Consultando todos los creditos",new RuntimeException());
        return creditProductService.getAllCreditProducts();
    }

    @GetMapping("/{id}")
    public Mono<CreditProduct> getCreditProductById(@PathVariable String id) {
         log.info("Consultando credito con id {}", id);
         log.debug("Consultando credito con id {}", id, new RuntimeException());
        return creditProductService.getCreditProductById(id);
    }
    @PostMapping
    public Mono<ResponseEntity<CreditProduct>> createCreditProduct(@Valid @RequestBody CreditProduct creditProduct) {
//         log.info("Creando credito {}", creditProduct);
//         log.debug("Creando credito {}", creditProduct, new RuntimeException());
         return creditProductService.createCreditProduct(creditProduct)
                 .map(createdCreditProduct -> ResponseEntity.status(HttpStatus.CREATED).body(createdCreditProduct))
                 .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()));

    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreditProduct>> updateCreditProduct(@PathVariable String id, @Valid @RequestBody CreditProduct creditProduct) {
        log.info("Actualizando credito {}", creditProduct);
        log.debug("Actualizando credito {}", creditProduct, new RuntimeException());
         return creditProductService.updateCreditProduct(id, creditProduct)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCreditProductById(@PathVariable String id) {
         log.info("Eliminando credito con id {}", id);
         log.debug("Eliminando credito con id {}", id, new RuntimeException());
        return creditProductService.deleteCreditProductById(id);
    }



}
