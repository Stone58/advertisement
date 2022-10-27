/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Adnaane
 */
@ControllerAdvice
public class DealerExceptionController {

    /**
     * This exception is thrown when someone try to use information about a dealer that doesn't exist in the database
     * @param exception
     * @return 
     */
    @ExceptionHandler(value = DealerNotDefinedException.class)
    public ResponseEntity<Object> exception(DealerNotDefinedException exception) {
        return new ResponseEntity<>("Dealer Id not found", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This exception happend when the user reach his limit and doesn't want to
     * unpublish the oldest published listing
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = DealerNumberLimitException.class)
    public ResponseEntity<Object> exception(DealerNumberLimitException exception) {
        return new ResponseEntity<>("Dealer has reached his number of limit", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
