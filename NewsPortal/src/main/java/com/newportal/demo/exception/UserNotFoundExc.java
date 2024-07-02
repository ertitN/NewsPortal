package com.newportal.demo.exception;




public class UserNotFoundExc  extends RuntimeException{
    public UserNotFoundExc(String message) {
        super(message);
    }
}
