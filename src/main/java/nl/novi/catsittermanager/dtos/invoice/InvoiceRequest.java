package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Validated
public record InvoiceRequest(

        @NotNull
        @FutureOrPresent
        LocalDate invoiceDate,
        @NotNull
        @Positive
        Double amount,
        @NotNull
        Boolean paid,
        UUID orderNo
) {
}
