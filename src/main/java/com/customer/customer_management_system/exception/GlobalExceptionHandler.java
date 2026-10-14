package com.customer.customer_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

//spring boot detects the validation errors
//GlobalExceptionHandler collects those errors and return a meaningful msg

//Common exception-handling login
//So if any exception occurs in controllers then this class handle those
//Gives exception handling behavior
//Advice - provides shared behavior to those controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    //This method will run for this type of error - MethodArgumentNotValidException
    //Class information to identify which exception type this method handle
    @ExceptionHandler(MethodArgumentNotValidException.class)

        //Ex is a variable
        //Receives the actual object from spring
        //Contains information about the validation failure
        public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){

            //Map because key value pairs
            //wraps the map in a http response
            //so can specify the response body and status code
            //So method returns an HTTP response contains map of validation errors.

            Map<String, String> errors = new HashMap<>();

            //getBindingResult retrieves validation information from the ex(exception)
            //getFieldErrors retrieves a collection of field-error objects
            //Java takes one error at a time
            //temporarily refer to it using variable error (one field-error object)
            ex.getBindingResult().getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        //Return errors map as a json response with http status code
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);


    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex){
        Map<String, String> errors = Map.of("message", ex.getReason());

        return new ResponseEntity<>(errors, ex.getStatusCode());
    }

}
