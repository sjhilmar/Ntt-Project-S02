package com.example.bank_customer.service.impl;

import com.example.bank_customer.domain.Customer;
import com.example.bank_customer.domain.CustomerType;
import com.example.bank_customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

@Mock
private CustomerRepository customerRepository;

@InjectMocks
private CustomerService customerService;

@BeforeEach
void setUp(){
    MockitoAnnotations.openMocks(this);
}

    @Test
    void getAllCustomer() {

        Customer customer1 = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL);
        Customer customer2 = new Customer("2", "456", "Company B", "Jane Doe", "jane@example.com", "0987654321", CustomerType.EMPRESARIAL);

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2));
        Flux<Customer> result = customerService.getAllCustomer();
        StepVerifier.create(result)
                .expectNext(customer1)
                .expectNext(customer2)
                .verifyComplete();
    }

    @Test
    void getCustomerById() {

        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL);
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.getCustomerById(customer.getId());
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void getCustomerByDocumentNumber() {

        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL);
        Mockito.when(customerRepository.findCustomerByDocumentNumber(customer.getDocumentNumber())).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.getCustomerByDocumentNumber(customer.getDocumentNumber());
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL);
        Mockito.when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.createCustomer(customer);
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void updateCustomer() {
        Customer existingCustomer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL);
        Customer updatedCustomer = new Customer("2", "456", "Company B", "Jane Doe", "jane@example.com", "0987654321", CustomerType.EMPRESARIAL);
        Mockito.when(customerRepository.findById(existingCustomer.getId())).thenReturn(Mono.just(existingCustomer));
        Mockito.when(customerRepository.save(existingCustomer)).thenReturn(Mono.just(updatedCustomer));
        Mono<Customer> result = customerService.updateCustomer(existingCustomer.getId(), updatedCustomer);
        StepVerifier.create(result)
                .expectNext(updatedCustomer)
                .verifyComplete();
    }

    @Test
    void deleteCustomerById() {
        Mockito.when(customerRepository.deleteById("1")).thenReturn(Mono.empty());
        Mono<Void> result = customerService.deleteCustomerById("1");
        StepVerifier.create(result)
            .verifyComplete();
    }
}