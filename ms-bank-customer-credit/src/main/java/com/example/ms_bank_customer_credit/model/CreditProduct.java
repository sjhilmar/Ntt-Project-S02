package com.example.ms_bank_customer_credit.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "creditProduct")
public class CreditProduct {

    @Id
    private String id;

    @NotEmpty(message = "Se requiere el codigo del cliente")
    private String customerId;

    @NotEmpty(message = "Se requiere nombre del titular")
    private String accountHolderName;

    @NotNull(message = "El monto del crédito no puede ser nulo")
    private BigDecimal creditAmount;

    private List<String> authorizedSigners; // For business credits
    private List<String> holders; // For business credits

    @NotNull(message = "Se requiere tipo de crédito")
    private CreditType creditType;

    @NotNull(message = "Se requiere tipo de cliente")
    private CustomerType customerType;
}
