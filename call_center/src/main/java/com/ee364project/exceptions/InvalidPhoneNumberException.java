package com.ee364project.exceptions;

public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String phoneNumber) {
        super("Invalid phone number" + phoneNumber);
    }
}
