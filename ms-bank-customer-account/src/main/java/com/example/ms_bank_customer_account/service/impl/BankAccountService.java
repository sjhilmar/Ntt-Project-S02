package com.example.ms_bank_customer_account.service.impl;

import com.example.ms_bank_customer_account.model.AccountType;
import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.model.CustomerType;
import com.example.ms_bank_customer_account.repository.BankAccountRepository;
import com.example.ms_bank_customer_account.service.IBankAccountService;
import com.example.ms_bank_customer_account.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Flux<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll()  ;
    }

    @Override
    public Flux<BankAccount> findByClientTypeAndAccountType(CustomerType customerType, AccountType accountType) {
        return bankAccountRepository.findByCustomerTypeAndAccountType(customerType,accountType);
    }

    @Override
    public Flux<BankAccount> findBankAccountByCustomerID(String customerID) {
        return bankAccountRepository.findBankAccountByCustomerId(customerID);
    }

    @Override
    public Mono<BankAccount> getAccountById(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    @Override
    public Mono<BankAccount> createBankAccount(BankAccount bankAccount) {
        if (bankAccount.getCustomerType()== CustomerType.PERSONAL){
            return   bankAccountRepository.findBankAccountByCustomerId(bankAccount.getCustomerId())
                    .collectList()
                    .flatMap(accounts->{
                        long savingCount =accounts.stream().filter(account->account.getAccountType()==AccountType.AHORRO).count();
                        long checkingCount = accounts.stream().filter(account->account.getAccountType()==AccountType.CORRIENTE).count();

                        if (bankAccount.getAccountType()==AccountType.AHORRO && savingCount >0){
                            return Mono.error(new CustomException("El cliente personal solo puede tener una cuenta de ahorros"));
                        }
                        if (bankAccount.getAccountType()==AccountType.CORRIENTE && checkingCount >0){
                            return Mono.error(new CustomException("El cliente personal solo puede tener una cuenta corriente"));
                        }
                        return bankAccountRepository.save(bankAccount);
                    });
        } else if (bankAccount.getCustomerType()== CustomerType.EMPRESARIAL){
            if (bankAccount.getAccountType()==AccountType.AHORRO || bankAccount.getAccountType()==AccountType.PLAZOFIJO){
                return Mono.error(new CustomException("El cliente empresarial no puede tener cuentas de ahorro ni de plazo fijo"));
            }
            if (bankAccount.getHolders() == null || bankAccount.getHolders().isEmpty()){
                return Mono.error(new CustomException("Las cuentas bancarias empresariales deben tener al menos un titular"));
            }
            return bankAccountRepository.save(bankAccount);
        }

        return Mono.error(new CustomException("Tipo de Cliente no valido"));
    }

    @Override
    public Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Cuenta no encontrada")))
                .flatMap(existingAccount ->{
                    existingAccount.setAccountNumber(bankAccount.getAccountNumber());
                    existingAccount.setAccountHolderName(bankAccount.getAccountHolderName());
                    existingAccount.setBalance(bankAccount.getBalance());
                    existingAccount.setAuthorizedSigners(bankAccount.getAuthorizedSigners());
                    existingAccount.setHolders(bankAccount.getHolders());
                    existingAccount.setAccountType(bankAccount.getAccountType());
                    existingAccount.setCustomerType(bankAccount.getCustomerType());
                    return bankAccountRepository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteBankAccount(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Cuenta no encontrada")))
                .flatMap(existingAccount -> bankAccountRepository.deleteById(id));
    }
}
