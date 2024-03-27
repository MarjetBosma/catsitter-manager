package nl.novi.catsittermanager.exceptions;

import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(HttpStatus notFound, String s) {
        super();
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
