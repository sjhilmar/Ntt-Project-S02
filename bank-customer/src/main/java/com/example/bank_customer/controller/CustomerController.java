package com.example.bank_customer.controller;

import com.example.bank_customer.domain.Customer;
import com.example.bank_customer.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/v1/customers")
@Tag(name="Customers", description = "Gestión de clientes")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping
    public Flux<Customer> getAllCustomer(){
        log.info("Obteniendo todos los clientes");
        return customerService.getAllCustomer();
    }

    @Operation(summary = "Obtener cliente por Id")
    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable String id){
        log.info("Obteniendo cliente por Id: {}",id);
        return customerService.getCustomerById(id);
    }

    @Operation(summary = "Obtener cliente por Número de documento")
    @GetMapping("/documentNumber/{documentNumber}")
    public Mono<Customer> getCustomerByDocumentNumber(@PathVariable String documentNumber){
        log.info("Obteniendo cliente por Número de documento: {}",documentNumber);
        return customerService.getCustomerByDocumentNumber(documentNumber);
    }

    @Operation(summary = "Creación de nuevos clientes")
    @PostMapping
    public Mono<ResponseEntity<Customer>> createCustomer(@RequestBody Customer customer){

        log.info("Creando un nuevo cliente: {}",customer);
        log.debug(customer.toString());
        return customerService.createCustomer(customer)
                .map(createdCustomer ->ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .onErrorResume(e ->{
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id,@RequestBody Customer customer){
        log.info("Actualiando cliente por Id: {}",id);

        return customerService.updateCustomer(id,customer)
                .map(ResponseEntity::ok)
                .onErrorResume(e ->{
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable String id){
        log.info("deleting customer with id: {}",id);
        log.debug(HttpStatus.NO_CONTENT.toString() + " " + id);
        return customerService.deleteCustomerById(id);

    }

}
