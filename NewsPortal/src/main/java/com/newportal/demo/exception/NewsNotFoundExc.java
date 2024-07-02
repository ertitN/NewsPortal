package com.newportal.demo.exception;

public class NewsNotFoundExc extends RuntimeException{
    public NewsNotFoundExc(String message) {
        super(message);
    }
}
