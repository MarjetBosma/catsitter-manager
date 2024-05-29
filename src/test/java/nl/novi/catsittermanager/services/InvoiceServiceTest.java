package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.InvoiceFactory;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    InvoiceService invoiceService;

    @Test
    void testGetAllInvoices_shouldFetchAllInvoicesOnTheList() {
        // Given
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        List<Invoice> expectedInvoiceList = List.of(expectedInvoice);

        when(invoiceRepository.findAll()).thenReturn(expectedInvoiceList);

        // When
        List<Invoice> actualInvoiceList = invoiceService.getAllInvoices();

        // Then
        assertEquals(expectedInvoiceList, actualInvoiceList);

        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void testGetAllInvoices_noInvoicesInDatabase_shouldReturnEmptyList() {
        // Given
        when(invoiceRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Invoice> invoiceResponseList = invoiceService.getAllInvoices();

        // Then
        assertTrue(invoiceResponseList.isEmpty());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void testGetInvoice_shouldFetchInvoiceWithSpecificId() {
        // Given
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        when(invoiceRepository.findById(expectedInvoice.getInvoiceNo())).thenReturn(Optional.of(expectedInvoice));

        // When
        Invoice resultInvoice = invoiceService.getInvoice(expectedInvoice.getInvoiceNo());

        // Then
        assertEquals(expectedInvoice, resultInvoice);
        verify(invoiceRepository, times(1)).findById(expectedInvoice.getInvoiceNo());
    }

    @Test
    void testGetInvoice_shouldFetchInvoiceWithSpecificId_RecordNotFoundException() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.getInvoice(invoiceId));

        // Then
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository).findById(invoiceId);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    void testCreateInvoice_shouldCreateANewInvoice() {
        // Given
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        Order expectedOrder = OrderFactory.randomOrder().build();
        UUID orderNo = expectedOrder.getOrderNo();

        when(orderService.getOrder(orderNo)).thenReturn(expectedOrder);
        when(invoiceRepository.save(expectedInvoice)).thenReturn(expectedInvoice);

        // When
        Invoice resultInvoice = invoiceService.createInvoice(expectedInvoice, orderNo);

        // Then
        assertEquals(expectedInvoice, resultInvoice);
        assertEquals(expectedOrder, resultInvoice.getOrder());

        verify(orderService, times(1)).getOrder(orderNo);
        verify(invoiceRepository, times(1)).save(expectedInvoice);
    }

    @Test
    void testCreateInvoice_shouldThrowExceptionWhenOrderNotFound() {
        // Given
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        UUID orderNo = UUID.randomUUID();

        when(orderService.getOrder(orderNo)).thenReturn(null);

        // When & Then
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.createInvoice(expectedInvoice, orderNo));
        assertEquals("Order not found.", exception.getMessage());

        verify(orderService, times(1)).getOrder(orderNo);
        verify(invoiceRepository, times(0)).save(expectedInvoice);
    }

    @Test
    void testExistsByOrderNo_shouldReturnTrueIfInvoiceExists() {
        // Given
        UUID orderNo = UUID.randomUUID();

        when(invoiceRepository.existsByOrder_OrderNo(orderNo)).thenReturn(true);

        // When
        boolean exists = invoiceService.existsByOrderNo(orderNo);

        // Then
        assertTrue(exists);
        verify(invoiceRepository, times(1)).existsByOrder_OrderNo(orderNo);
    }

    @Test
    void testExistsByOrderNo_shouldReturnFalseIfInvoiceDoesNotExist() {
        // Given
        UUID orderNo = UUID.randomUUID();

        when(invoiceRepository.existsByOrder_OrderNo(orderNo)).thenReturn(false);

        // When
        boolean exists = invoiceService.existsByOrderNo(orderNo);

        // Then
        assertFalse(exists);
        verify(invoiceRepository, times(1)).existsByOrder_OrderNo(orderNo);
    }

    @Test
    void testEditInvoice_shouldEditExistingInvoice() {
        // Given
        Invoice invoice = InvoiceFactory.randomInvoice().build();

        when(invoiceRepository.findById(invoice.getInvoiceNo())).thenReturn(Optional.of(invoice));
        when(orderService.getOrder(invoice.getOrder().getOrderNo())).thenReturn(invoice.getOrder());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        // When
        Invoice resultInvoice = invoiceService.editInvoice(invoice.getInvoiceNo(), invoice, invoice.getOrder().getOrderNo());

        // Then
        assertEquals(invoice, resultInvoice);

        verify(invoiceRepository, times(1)).findById(invoice.getInvoiceNo());
        verify(orderService, times(1)).getOrder(invoice.getOrder().getOrderNo());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testEditInvoice_nonExistingInvoice_shouldThrowRecordNotFoundException() {
        // Given
        UUID invoiceNo = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceNo)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.getInvoice(invoiceNo));

        // Then
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(invoiceNo);
    }

    @Test
    void testDeleteInvoice_ShouldDeleteInvoiceWithSpecificId() {
        // Given
        Invoice invoice = InvoiceFactory.randomInvoice().build();
        when(invoiceRepository.existsById(invoice.getInvoiceNo())).thenReturn(true);

        // When
        UUID invoiceNo = invoiceService.deleteInvoice(invoice.getInvoiceNo());

        // Then
        verify(invoiceRepository, times(1)).existsById(invoiceNo);
        verify(invoiceRepository, times(1)).deleteById(invoiceNo);
        verifyNoInteractions(orderService);
    }

    @Test
    void testDeleteInvoice_ShouldThrowRecordNotFoundException() {
        // Given
        UUID invoiceNo = UUID.randomUUID();
        when(invoiceRepository.existsById(invoiceNo)).thenReturn(false);

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.deleteInvoice(invoiceNo));

        // Then
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).existsById(invoiceNo);
        verify(invoiceRepository, never()).deleteById(invoiceNo);
        verifyNoInteractions(orderService);
    }

}
