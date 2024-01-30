package nl.novi.catsittermanager.dtos.invoice;

import nl.novi.catsittermanager.models.Order;

import java.time.LocalDate;

public class InvoiceDto {
    public Long InvoiceNo;
    public LocalDate invoiceDate;
    public double amount;

    public Order order;

}
