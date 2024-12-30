package com.example.ms_bank_transaction.model;

import com.example.ms_bank_transaction.model.enums.CustomerType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Customer {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "Se requiere número del documento")
    private String documentNumber;
    @NotEmpty(message = "Se requiere Nombre de la compañía o nombre completo")
    private String companyName;

    @NotEmpty(message = "Se requiere Nombre del titular")
    private String name;
    private String email;
    private String phoneNumber;

    @NotEmpty(message = "Se requiere Tipo de cliente")
    private CustomerType customerType;
}
