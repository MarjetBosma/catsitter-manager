package nl.novi.catsittermanager.dtos.invoice;

import java.time.LocalDate;
import java.util.UUID;

public record InvoiceResponse(
        UUID invoiceNo,
        LocalDate invoiceDate,
        Double amount,
        Boolean paid,
        UUID orderNo

) {
}
