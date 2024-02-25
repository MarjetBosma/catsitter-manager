package nl.novi.catsittermanager.dtos.invoice;

import nl.novi.catsittermanager.models.Order;

import java.time.LocalDate;

public record InvoiceDto (
    Long invoiceNo,
    LocalDate invoiceDate,
    Double amount,
    Boolean paid, // true/false omzetten naar ja/nee

//    Order order
    String order // Dummy, alleen voor los testen Invoice class zonder database

) {}
