package com.ee364project.exceptions;

public class InvalidRatioValueException extends Exception {
    public InvalidRatioValueException(double value) {
        super("Ratio must be from 0 to 1 not: " + value);
    }
}
