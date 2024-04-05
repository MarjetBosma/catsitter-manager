package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Validated
public record InvoiceRequest(

        @NotNull
        LocalDate invoiceDate,
        @NotNull
        @Positive
        Double amount,
        @NotNull
        Boolean paid,

        UUID orderNo

) {
}
