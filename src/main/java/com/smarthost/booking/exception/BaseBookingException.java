package com.smarthost.booking.exception;

public abstract class BaseBookingException extends RuntimeException {
    public BaseBookingException(String message) {
        super(message);
    }
}
