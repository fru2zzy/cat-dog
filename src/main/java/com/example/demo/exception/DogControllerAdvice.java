package com.example.demo.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@ControllerAdvice
public class DogControllerAdvice {

    @ExceptionHandler(value = DogNotFoundException.class)
    public ResponseEntity<String> dogException(DogNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptionMessage(MethodArgumentNotValidException ex) {
        HashMap<String, Object> errors = new HashMap<>();

        errors.put("timeStamp", new Date().toString());
        errors.put("status", ex.getBindingResult().toString());

        List<String> errorsList = ex.getBindingResult().getFieldErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList());
        errors.put("errorsList", errorsList);
        return new ResponseEntity<>(errors, null, HttpStatus.NOT_FOUND);
    }

}
