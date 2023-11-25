package com.ee364project.exceptions;

public class InvalidIdException extends Exception {
    public InvalidIdException(String id) {
        super("Invalid id: " + id);
    }    
}
