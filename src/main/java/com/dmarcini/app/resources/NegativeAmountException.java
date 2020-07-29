package com.dmarcini.app.resources;

public final class NegativeAmountException extends Exception {
    public NegativeAmountException(String errorMessage) {
        super(errorMessage);
    }
}
