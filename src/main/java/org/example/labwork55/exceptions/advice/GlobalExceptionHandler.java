package org.example.labwork55.exceptions.advice;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.exceptions.ErrorResponseBody;
import org.example.labwork55.exceptions.QuestionNotFoundException;
import org.example.labwork55.exceptions.QuizNotFoundException;
import org.example.labwork55.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setTitle("User Not Found");
        body.setResponse(Map.of("errors", List.of(e.getMessage())));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleQuizNotFoundException(QuizNotFoundException e) {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setTitle("Quiz Not Found");
        body.setResponse(Map.of("errors", List.of(e.getMessage())));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleQuestionNotFoundException(QuestionNotFoundException e) {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setTitle("Question Not Found");
        body.setResponse(Map.of("errors", List.of(e.getMessage())));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> List.of(errorMessage));
        });

        ErrorResponseBody body = new ErrorResponseBody();
        body.setTitle("Validation Error");
        body.setResponse(errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleGeneralException(Exception e) {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setTitle("Error");
        body.setResponse(Map.of("errors", List.of(e.getMessage())));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}