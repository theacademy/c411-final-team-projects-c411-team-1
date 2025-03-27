package com.mthree.etrade.service;

public class APILimitReachException extends RuntimeException {
    public APILimitReachException(String message) {
        super(message);
    }
    public APILimitReachException(String message, Throwable cause) {
        super(message, cause);
    }
}
