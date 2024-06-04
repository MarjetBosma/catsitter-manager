package nl.novi.catsittermanager.dtos.invoice;

import java.util.UUID;

public record InvoiceResponse(

        UUID invoiceNo,
        String invoiceDate,
        Double amount,
        Boolean paid,
        UUID orderNo

) {
}
