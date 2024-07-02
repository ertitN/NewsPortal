package com.newportal.demo.exception;

public class UserNameAlreadyExistsExc extends RuntimeException{
    public UserNameAlreadyExistsExc(String message) {
        super(message);
    }
}
