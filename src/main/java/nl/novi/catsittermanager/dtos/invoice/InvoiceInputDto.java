package nl.novi.catsittermanager.dtos.invoice;

import nl.novi.catsittermanager.models.Order;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record InvoiceInputDto (

    Long invoiceNo, //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
    LocalDate invoiceDate,
    @Positive
    Double amount,
    Boolean paid,

//    Order order
    String order // Dummy, alleen voor los testen Invoice class zonder database

) {}
