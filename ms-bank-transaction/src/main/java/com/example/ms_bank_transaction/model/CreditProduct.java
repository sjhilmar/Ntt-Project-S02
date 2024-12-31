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
public class CreditProduct {
    @Id
    private String id;

    @NotEmpty(message = "Se requiere el codigo del cliente")
    private String customerId;

    @NotEmpty(message = "Se requiere nombre del titular")
    private String accountHolderName;

    @NotNull(message = "El monto del crédito no puede ser nulo")
    private BigDecimal creditAmount;

    @NotNull (message = "El saldo del crédito no puede ser nulo")
    private BigDecimal creditBalance;

    private List<String> authorizedSigners; // For business credits
    private List<String> holders; // For business credits

    @NotNull(message = "Se requiere tipo de crédito")
    private ProductType creditType;

    @NotNull(message = "Se requiere tipo de cliente")
    private CustomerType customerType;

    @NotNull(message = "Se requiere el numero maximo de transacciones")
    private int numberMaxTransactions;
}
