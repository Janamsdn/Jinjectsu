package com.ericlouw.jinjectsu.jinjectsu.exceptions;

public class InjectionException extends RuntimeException {
    public InjectionException(String message, Throwable throwable){
        super(message, throwable);
    }
}
