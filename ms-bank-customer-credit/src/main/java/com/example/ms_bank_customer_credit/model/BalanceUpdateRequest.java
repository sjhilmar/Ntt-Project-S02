package com.example.ms_bank_customer_credit.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class BalanceUpdateRequest {
    @NotNull(message = "El balance no puede ser nulo")
    @Positive(message = "El balance debe ser un valor positivo")
    private BigDecimal creditBalance;
}
