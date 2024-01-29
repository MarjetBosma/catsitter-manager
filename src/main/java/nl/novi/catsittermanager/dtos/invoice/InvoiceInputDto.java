package nl.novi.catsittermanager.dtos.invoice;

import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class InvoiceInputDto {
    public Long InvoiceNo;
    public LocalDate invoiceDate;
    @Positive
    public double amount;
}
