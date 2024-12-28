package com.example.ms_bank_customer_account.util;

import lombok.Getter;


public class CustomException extends  RuntimeException{

    public CustomException(String message) {
        super(message);
    }

}
