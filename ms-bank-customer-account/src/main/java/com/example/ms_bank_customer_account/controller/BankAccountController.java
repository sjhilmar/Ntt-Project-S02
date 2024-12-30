package com.example.ms_bank_customer_account.controller;

import com.example.ms_bank_customer_account.dto.ErrorResponse;
import com.example.ms_bank_customer_account.model.BalanceUpdateRequest;
import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.service.IBankAccountService;
import com.example.ms_bank_customer_account.util.CustomException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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
        return service.getAllBankAccounts()
                .doOnError(e-> log.error("Error al obtener todas las cuentas bancarias",e));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> getAccountById(@PathVariable String id) {
        log.info("Obteniendo cuenta bancaria con id: " + id);
        return service.getAccountById(id)
                .map(ResponseEntity::ok)
                .doOnError(e->log.error("Error al obtener la cuenta bancaria con id: " + id, e))
                .onErrorResume(e->Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));

    }

    @PostMapping
    public Mono<ResponseEntity<BankAccount>> createAccount(@Valid @RequestBody BankAccount account) {
        log.info("Creando cuenta bancaria/n{}", account.toString());
        return service.createBankAccount(account)
                .map(createdCustomer-> ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .doOnError(e-> log.error("Error al crear la cuenta bancaria\n{}", account,e))
                .onErrorResume(CustomException.class, e-> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<BankAccount>build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> updateAccount(@PathVariable String id, @Valid @RequestBody BankAccount account) {
        log.info("Actualizando cuenta bancaria con id: " + id);
        return service.updateBankAccount(id, account)
                .map(updatedAccount-> ResponseEntity.status(HttpStatus.OK).body(updatedAccount))
                .doOnError(e->log.error("Error al actualizar la cuenta bancaria con id: " + id, e))
                .onErrorResume(CustomException.class, Mono::error)
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PatchMapping("/balance/{id}")
    public Mono<ResponseEntity<BankAccount>> updateBalance(@PathVariable String id, @Valid @RequestBody BalanceUpdateRequest balanceUpdateRequest){
        log.info("Actualizando balance de la cuenta bancaria con id: " + id);
        return service.updateBalance(id, balanceUpdateRequest.getBalance())
                .map(updatedAccount-> ResponseEntity.status(HttpStatus.OK).body(updatedAccount))
                .doOnError(e->log.error("Error al actualizar el balance de la cuenta bancaria con id: " + id, e))
                .onErrorResume(CustomException.class, Mono::error)
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String id) {
        log.info("Eliminando cuenta bancaria con id: " + id);
        return service.deleteBankAccount(id)
                .doOnError(e-> log.error("Error al eliminar la cuenta bancaria con id: {}", id, e))
                .onErrorResume(e->Mono.error(new CustomException("Error al eliminar la cuenta bancaria con id: " + id)));
    }

}
