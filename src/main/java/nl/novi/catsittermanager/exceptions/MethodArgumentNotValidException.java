package nl.novi.catsittermanager.exceptions;

public class MethodArgumentNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MethodArgumentNotValidException() {
        super();
    }

    public MethodArgumentNotValidException(String message) {
        super(message);
    }
}
