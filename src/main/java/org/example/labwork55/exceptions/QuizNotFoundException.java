package org.example.labwork55.exceptions;

import java.util.NoSuchElementException;

public class QuizNotFoundException extends NoSuchElementException {
    public QuizNotFoundException(String message) {
        super(message);
    }
}