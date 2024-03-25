package nl.novi.catsittermanager.dtos.invoice;

import nl.novi.catsittermanager.models.Order;

import java.time.LocalDate;
import java.util.UUID;

public record InvoiceDto(
        UUID invoiceNo,
        LocalDate invoiceDate,
        Double amount,
        Boolean paid,
        Order order

) {
}
