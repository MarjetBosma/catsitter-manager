package nl.novi.catsittermanager.exceptions;

import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(HttpStatus notFound, String s) {
        super(s);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
