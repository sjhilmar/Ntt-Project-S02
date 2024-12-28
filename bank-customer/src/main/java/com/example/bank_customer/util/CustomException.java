package com.example.bank_customer.util;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException{
    private String code;
    private HttpStatus status;

    public CustomException(String code,HttpStatus status , String message){
        super(message);
        this.status=status;
        this.code = code;

    }
}
