package org.example.labwork55.exceptions;

import java.util.NoSuchElementException;

public class QuestionNotFoundException extends NoSuchElementException {
    public QuestionNotFoundException(String message) {
        super(message);
    }
}