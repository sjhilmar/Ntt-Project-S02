package com.example.ms_bank_customer_account.util;

import com.example.ms_bank_customer_account.model.enums.AccountType;
import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.model.enums.CustomerType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BankAccountUtil {

    public static List<BankAccount> insertDataBankAccount(){
        return Stream.of(
                new BankAccount("1", "1", "4444-4444-4444-4444", "Sergio Jhilmar",
                        new BigDecimal("1500.00"), Collections.singletonList(""),
                        Collections.singletonList(""), AccountType.CORRIENTE, CustomerType.EMPRESARIAL,50,false),

                new BankAccount("2", "3", "4444-4444-4444-3333", "Juan Carguas",
                        new BigDecimal("1000.00"), Collections.singletonList("Sergio Jhilmar"),
                        Collections.singletonList("Marco Braulio"), AccountType.CORRIENTE, CustomerType.EMPRESARIAL,50,false),

                new BankAccount("3", "3", "4444-4444-4444-0404", "Juan Carguas",
                        new BigDecimal("1000.00"), Collections.singletonList("Sergio Jhilmar"),
                        Collections.singletonList("Marco Braulio"), AccountType.CORRIENTE, CustomerType.EMPRESARIAL,50,false),

                new BankAccount("4", "4", "4444-4444-4444-2222", "Antonieta Loyola",
                        new BigDecimal("1800.00"), Collections.singletonList(""),
                        Collections.singletonList(""), AccountType.AHORRO, CustomerType.PERSONAL,50,false),

                new BankAccount("5", "4", "4444-4444-4444-2222", "Antonieta Loyola",
                        new BigDecimal("1800.00"), Collections.singletonList(""),
                        Collections.singletonList(""), AccountType.PLAZOFIJO, CustomerType.PERSONAL,50,false)
        ).collect(Collectors.toList());
    }

}
