package com.example.ms_bank_transaction.controller;


import com.example.ms_bank_transaction.model.Transaction;
import com.example.ms_bank_transaction.service.ITransactionService;
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
@RequestMapping("/v1/transaction")
public class TransactionController {
    @Autowired
    private ITransactionService service;

    @GetMapping
    public Flux<Transaction> getAllTransaction(){
        log.info("Obteniendo todas las transacciones");
        return service.getAllTransactions()
                .doOnError(e-> log.error("Error al obtener todas las transacciones",e));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Transaction>>getTransactionById(String id){
        log.info("Obteniendo transacción con id: " + id);
        return service.getTransactionById(id)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al obtener la transacción con id: " + id, e))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping("/product/{productId}")
    public Flux<ResponseEntity<Transaction>> getTransactionsByProductId(String productId){
        log.info("Obteniendo transacciones con productId: " + productId);
        return service.getTransactionByProductId(productId)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al obtener las transacciones con productId: " + productId, e))
                .onErrorResume(e-> Flux.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Transaction>> createTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("Creando transacción\n{}", transaction.toString());
        return service.createTransaction(transaction)
                .map(createdTransaction -> ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction))
                .doOnError(e -> log.error("Error al crear la transacción\n{}", transaction, e))
                .onErrorResume(RuntimeException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Transaction>> updateTransaction(@PathVariable String id, @Valid @RequestBody Transaction transaction) {
        log.info("Actualizando transacción con id: " + id);
        return service.updateTransaction(id, transaction)
                .map(updatedTransaction -> ResponseEntity.status(HttpStatus.OK).body(updatedTransaction))
                .doOnError(e -> log.error("Error al actualizar la transacción con id: " + id, e))
                .onErrorResume(RuntimeException.class, Mono::error)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTransaction(@PathVariable String id) {
        log.info("Eliminando transacción con id: " + id);
        return service.deleteTransactionById(id)
                .doOnError(e -> log.error("Error al eliminar la transacción con id: " + id, e))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al eliminar la transacción con id: " + id)));
    }




}
