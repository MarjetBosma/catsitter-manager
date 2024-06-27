package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InvoiceMapper {

    public static InvoiceResponse InvoiceToInvoiceResponse(Invoice invoice) {

        return new InvoiceResponse(
                invoice.getInvoiceNo(),
                invoice.getInvoiceDate().toString(),
                invoice.getAmount(),
                invoice.getPaid(),
                invoice.getOrder().getOrderNo()
        );
    }

    public static Invoice InvoiceRequestToInvoice(InvoiceRequest invoiceRequest, Order order) {

//        Order order = new Order();
//        order.setOrderNo(invoiceRequest.orderNo());
//        order.setTasks(new ArrayList<>());

        return Invoice.builder()
                .invoiceDate(LocalDate.parse(invoiceRequest.invoiceDate()))
                .amount(order.calculateTotalCost())
                .paid(invoiceRequest.paid())
                .order(order)
                .build();
    }
}
