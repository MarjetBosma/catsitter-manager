package nl.novi.catsittermanager.exceptions;

public class InvoiceAlreadyExistsForThisOrderException extends RuntimeException {
    public InvoiceAlreadyExistsForThisOrderException(String message) {
        super(message);
    }
}