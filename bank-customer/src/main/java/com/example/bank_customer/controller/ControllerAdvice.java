package com.example.bank_customer.controller;

import com.example.bank_customer.dto.ErrorDTO;
import com.example.bank_customer.util.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorDTO> customExceptionHandler(CustomException e){
        ErrorDTO errorDTO= ErrorDTO.builder().code(e.getCode()).message(e.getMessage()).build();
        return new ResponseEntity<>(errorDTO,e.getStatus());
    }
}
