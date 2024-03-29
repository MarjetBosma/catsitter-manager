package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Validated
public record InvoiceInputDto(

        @FutureOrPresent
        LocalDate invoiceDate,
        @Positive
        Double amount,
        Boolean paid,
        UUID orderNo
) {
}
