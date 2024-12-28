package com.example.ms_bank_customer_account.controller;

import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.service.IBankAccountService;
import com.example.ms_bank_customer_account.service.impl.BankAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/bank-account")
@Tag(name = "Bank Account", description = "Endpoints for bank account management")
public class BankAccountController {

    @Autowired
    private IBankAccountService service;

    @GetMapping
    public Flux<BankAccount> getAllBankAccounts(){
        log.info("Obteniendo todas las cuentas bancarias");
        log.debug(HttpStatus.OK.toString( ));
        return service.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public Mono<BankAccount> getAccountById(@PathVariable String id) {
        log.info("Obteniendo cuenta bancaria con id: " + id);
        log.debug(HttpStatus.OK.toString( ));
        return service.getAccountById(id);
    }

    @PostMapping

    public Mono<ResponseEntity<BankAccount>> createAccount(@Valid @RequestBody BankAccount account) {
        log.info("Creando cuenta bancaria");
        log.debug(account.toString());
        return service.createBankAccount(account)
                .map(createdCustomer-> ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .onErrorResume(e-> {
                   return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null));
                });

    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> updateAccount(@PathVariable String id, @Valid @RequestBody BankAccount account) {
        log.info("Actualizando cuenta bancaria con id: " + id);
        log.debug(account.toString());
        return service.updateBankAccount(id, account)
                .map(updatedAccount-> ResponseEntity.status(HttpStatus.OK).body(updatedAccount))
                .onErrorResume(e->{
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String id) {
        log.info("Eliminando cuenta bancaria con id: " + id);
        log.debug(HttpStatus.OK.toString( ) + id);
        return service.deleteBankAccount(id);
    }

}
