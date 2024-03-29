package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.FutureOrPresent;
import nl.novi.catsittermanager.models.Order;

import jakarta.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record InvoiceInputDto (

    @FutureOrPresent
    LocalDate invoiceDate,
    @Positive
    Double amount,
    Boolean paid,
    Order order

) {
}
