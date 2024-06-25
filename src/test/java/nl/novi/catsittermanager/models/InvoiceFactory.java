package nl.novi.catsittermanager.models;

import net.datafaker.Faker;
import nl.novi.catsittermanager.helpers.InvoiceFactoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceFactory {

    private static final Faker faker = new Faker();

    public static Invoice.InvoiceBuilder randomInvoice() {
        return Invoice.builder()
                .invoiceNo(UUID.randomUUID())
                .invoiceDate(InvoiceFactoryHelper.randomDateIn2024())
                .amount(null)
                .paid(faker.bool().bool())
                .order(null);
    }

    public static List<Invoice> randomInvoices(int count) {
        List<Invoice> invoices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            invoices.add(randomInvoice().build());
        }
        return invoices;
    }
}
