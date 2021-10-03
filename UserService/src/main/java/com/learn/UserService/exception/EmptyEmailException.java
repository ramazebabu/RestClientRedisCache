package com.learn.UserService.exception;

public class EmptyEmailException extends RuntimeException{
    public EmptyEmailException(String message) {
        super(message);
    }
}
