package com.vetis.integrationmodule;

public class VetisHelperException extends Exception {
    public VetisHelperException(String message) {
        super("Parse error occurred! " + message);
    }
}
