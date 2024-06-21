package nl.novi.catsittermanager.exceptions;

import java.util.UUID;

public class InvoiceAlreadyExistsForThisOrderException extends RuntimeException {
    public InvoiceAlreadyExistsForThisOrderException(UUID orderNo) {
        super("An invoice already exists for order " + orderNo + ".");
    }
}