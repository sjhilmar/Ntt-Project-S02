package com.example.ms_bank_customer_account.service.impl;

import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.repository.BankAccountRepository;
import com.example.ms_bank_customer_account.util.BankAccountUtil;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBankAccounts() {

        List<BankAccount> listBankAccount =  BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.findAll()).thenReturn(Flux.fromIterable(listBankAccount));
        Flux<BankAccount> result = bankAccountService.getAllBankAccounts();
        StepVerifier.create(result)
                .expectNextSequence(listBankAccount)
                .verifyComplete();
    }

    @Test
    void findBankAccountByCustomerID() {
        List<BankAccount> listBankAccount =  BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.findBankAccountByCustomerId(listBankAccount.get(0).getCustomerId())).thenReturn(Flux.fromIterable(listBankAccount));
        Flux<BankAccount> result = bankAccountService.findBankAccountByCustomerID(listBankAccount.get(0).getCustomerId());
        StepVerifier.create(result)
                .expectNextSequence(listBankAccount)
                .verifyComplete();
    }

    @Test
    void getAccountById() {
        List<BankAccount> listBankAccount =  BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.findById(listBankAccount.get(0).getId())).thenReturn(Mono.just(listBankAccount.get(0)));
        Mono<BankAccount> result = bankAccountService.getAccountById(listBankAccount.get(0).getId());
        StepVerifier.create(result)
                .expectNext(listBankAccount.get(0))
                .verifyComplete();
    }

    @Test
    void createBankAccount() {
        List<BankAccount> listBankAccount =  BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.save(listBankAccount.get(0))).thenReturn(Mono.just(listBankAccount.get(0)));
        Mono<BankAccount> result = bankAccountService.createBankAccount(listBankAccount.get(0));
        StepVerifier.create(result)
                .expectNext(listBankAccount.get(0))
                .verifyComplete();
    }

    @Test
    void updateBankAccount() {
        List<BankAccount> listBankAccount =  BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.findById(listBankAccount.get(0).getId())).thenReturn(Mono.just(listBankAccount.get(0)));
        Mockito.when(bankAccountRepository.save(listBankAccount.get(0))).thenReturn(Mono.just(listBankAccount.get(0)));
        Mono<BankAccount> result = bankAccountService.updateBankAccount(listBankAccount.get(0).getId(), listBankAccount.get(0));
        StepVerifier.create(result)
                .expectNext(listBankAccount.get(0))
                .verifyComplete();
    }

    @Test
    void deleteBankAccount() {

        List<BankAccount> listBankAccount = BankAccountUtil.insertDataBankAccount();
        Mockito.when(bankAccountRepository.findById(listBankAccount.get(0).getId())).thenReturn(Mono.just(listBankAccount.get(0)));
        Mockito.when(bankAccountRepository.deleteById(listBankAccount.get(0).getId())).thenReturn(Mono.empty());

        Mono<Void> result = bankAccountService.deleteBankAccount(listBankAccount.get(0).getId());

        StepVerifier.create(result)
                .verifyComplete();

    }
}