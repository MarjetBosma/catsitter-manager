package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Record not found");
        responseBody.put("message", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(value = FileNotFoundException.class)
//    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exception) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }

    @ExceptionHandler(value = nl.novi.catsittermanager.exceptions.FileNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFileNotFoundException(nl.novi.catsittermanager.exceptions.FileNotFoundException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<String> handleInvalidTypeException(InvalidTypeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFound(UsernameNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvoiceAlreadyExistsForThisOrderException.class)
    public ResponseEntity<Object> handleInvoiceAlreadyExistsForThisOrderException(InvoiceAlreadyExistsForThisOrderException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT); // You can use HttpStatus.CONFLICT for username conflict
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
