package com.spring.springsecurity.exception;

public class EntityException extends Exception{
    public EntityException(String message) {
        super(message);
    }
    public EntityException() {
        super("An error occurred.");
    }

}
