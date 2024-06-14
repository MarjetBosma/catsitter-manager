package nl.novi.catsittermanager.exceptions;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileNotFoundException(HttpStatus notFound, String s) {
        super(s);
    }

    public FileNotFoundException(String message) {
        super(message);
    }

}
