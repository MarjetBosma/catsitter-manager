package nl.novi.catsittermanager.dtos.invoice;

import net.datafaker.Faker;

import nl.novi.catsittermanager.helpers.InvoiceFactoryHelper;

import java.util.UUID;

public class InvoiceRequestFactory {

    private static final Faker faker = new Faker();

    public static InvoiceRequest.InvoiceRequestBuilder randomInvoiceRequest() {

        return InvoiceRequest.builder()
                .invoiceDate(InvoiceFactoryHelper.randomDateIn2024().toString())
                .amount((double) faker.number().numberBetween(50, 300))
                .paid(faker.bool().bool())
                .orderNo(UUID.randomUUID());
    }
}
