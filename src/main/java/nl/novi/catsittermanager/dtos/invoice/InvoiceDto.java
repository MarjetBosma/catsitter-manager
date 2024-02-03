package nl.novi.catsittermanager.dtos.invoice;

import nl.novi.catsittermanager.models.Order;

import java.time.LocalDate;

public class InvoiceDto {
    public Long invoiceNo;
    public LocalDate invoiceDate;
    public Double amount;

    public Order order;

}
