package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepos;

    private final OrderRepository orderRepos;

    private final OrderService orderService;

    public InvoiceService(InvoiceRepository invoiceRepos, OrderRepository orderRepos, OrderService orderService) {
        this.invoiceRepos = invoiceRepos;
        this.orderRepos = orderRepos;
        this.orderService = orderService;
    }

    public InvoiceDto TransferToDto(Invoice invoice) {

        InvoiceDto invoiceDto = new InvoiceDto();

        invoiceDto.invoiceNo = invoice.getInvoiceNo();
        invoiceDto.invoiceDate = invoice.getInvoiceDate();
        invoiceDto.amount = invoice.getAmount();
        invoiceDto.order = invoice.getOrder();

        return invoiceDto;
    }

    public Invoice TransferToInvoice(InvoiceInputDto invoiceDto) {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNo(invoiceDto.invoiceNo);
        invoice.setInvoiceDate(invoiceDto.invoiceDate);
        invoice.setAmount(invoiceDto.amount);
        invoice.setOrder(invoiceDto.order);

        return invoice;
    }
}
