package com.learn.liquibase.demo.exception;

public class EmptyEmailException extends RuntimeException{
    public EmptyEmailException(String message) {
        super(message);
    }
}
