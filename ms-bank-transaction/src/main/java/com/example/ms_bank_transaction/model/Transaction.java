package com.example.ms_bank_transaction.model;

import com.example.ms_bank_transaction.model.enums.ProductType;
import com.example.ms_bank_transaction.model.enums.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "bank-transaction")
public class Transaction {
    @Id
    private String id;

    @NotEmpty(message = "Se requiere el ID del producto")
    private String productId;

    @NotNull(message = "Se requiere el tipo de producto")
    private ProductType productType;

    @NotNull(message = "Se requiere el tipo de transacción")
    private TransactionType transactionType;

    @NotNull(message = "El monto no puede ser nulo")
    private BigDecimal amount;

    @NotNull(message = "La comisión no puede ser nula")
    private BigDecimal comission;

    private LocalDateTime transactionDate;

}
