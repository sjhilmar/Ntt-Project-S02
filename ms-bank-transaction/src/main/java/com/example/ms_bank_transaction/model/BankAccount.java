package com.example.ms_bank_transaction.model;

import com.example.ms_bank_transaction.model.enums.CustomerType;
import com.example.ms_bank_transaction.model.enums.ProductType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankAccount {
    @Id
    private String id;

    @NotEmpty(message = "Se requiere el codigo del cliente")
    private String customerId;

    @NotEmpty(message = "Se requiere numero de cuenta")
    private String accountNumber;

    @NotEmpty(message = "Se requiere nombre del titular")
    private String accountHolderName;

    @NotNull(message = "El saldo minimo es cero")
    private BigDecimal balance;

    private List<String> authorizedSigners;
    private List<String> holders;

    @NotNull(message = "Se requiere tipo de cuenta")
    private ProductType accountType;

    @NotNull(message = "Se requiere tipo de cliente")
    private CustomerType customerType;

    @NotNull(message = "Se requiere el numero maximo de transacciones")
    private int numberMaxTransactions;

    @NotNull(message = "Se requiere indicar si tiene tarjeta de crédito")
    private boolean hasCreditCard;
}
