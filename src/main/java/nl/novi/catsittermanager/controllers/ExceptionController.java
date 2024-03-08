package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.exceptions.BadRequestException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> badRequest(BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> recordNotFound(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
