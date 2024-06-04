package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Builder
public record InvoiceRequest(

        @NotNull
        String invoiceDate,
        @NotNull
        @Positive
        Double amount,
        @NotNull
        Boolean paid,
        UUID orderNo

) {
}
