package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Builder
public record InvoiceRequest(
        @NotNull(message = "Invoice date is required")
        String invoiceDate,
//        @NotNull(message = "Amount to pay is required")
//        @Positive
//        Double amount,
        @NotNull(message = "Pay status is required")
        Boolean paid,
        @NotNull(message = "Give the number of the order to which this invoice belongs")
        UUID orderNo
) {
}
