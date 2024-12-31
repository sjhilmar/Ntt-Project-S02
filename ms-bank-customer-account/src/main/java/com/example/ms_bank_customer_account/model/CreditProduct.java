package com.example.ms_bank_customer_account.model;

import com.example.ms_bank_customer_account.model.enums.CreditType;
import com.example.ms_bank_customer_account.model.enums.CustomerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private CreditType creditType;

    @NotNull(message = "Se requiere tipo de cliente")
    private CustomerType customerType;

    @NotNull(message = "Se requiere el numero maximo de transacciones")
    private int numberMaxTransactions;


}
