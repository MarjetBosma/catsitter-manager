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
        Double amount,
        Boolean paid,
        @NotNull(message = "Give the number of the order to which this invoice belongs")
        UUID orderNo
) {
}
