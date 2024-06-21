package nl.novi.catsittermanager.exceptions;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException(String type) {
        super("Invalid type: " + type);
    }
}