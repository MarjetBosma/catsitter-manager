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
        UUID invoiceId = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();
        Invoice existingInvoice = InvoiceFactory.randomInvoice().invoiceNo(invoiceId).build();
        Invoice updatedInvoice = InvoiceFactory.randomInvoice().build();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(existingInvoice);

        // When
        Invoice resultInvoice = invoiceService.editInvoice(invoiceId, updatedInvoice, orderNo);

        // Then
        assertEquals(existingInvoice.getInvoiceNo(), resultInvoice.getInvoiceNo());
        assertEquals(updatedInvoice.getInvoiceDate(), resultInvoice.getInvoiceDate());
        assertEquals(updatedInvoice.getAmount(), resultInvoice.getAmount());
        assertEquals(updatedInvoice.getPaid(), resultInvoice.getPaid());

        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(invoiceRepository, times(1)).save(existingInvoice);
    }

    @Test
    void testEditInvoice_nonExistingInvoice_shouldThrowRecordNotFoundException() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        Invoice updatedInvoice = InvoiceFactory.randomInvoice().build();
        UUID orderNo = UUID.randomUUID();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.editInvoice(invoiceId, updatedInvoice, orderNo));

        // Then
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(invoiceId);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    void testDeleteInvoice_ShouldDeleteInvoiceWithSpecificId() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.existsById(invoiceId)).thenReturn(true);

        // When
        UUID resultInvoiceId = invoiceService.deleteInvoice(invoiceId);

        // Then
        verify(invoiceRepository, times(1)).existsById(invoiceId);
        verify(invoiceRepository, times(1)).deleteById(invoiceId);
        verifyNoMoreInteractions(invoiceRepository);
        assertEquals(invoiceId, resultInvoiceId);
    }

    // Test for deleting a non-existing invoice
    @Test
    void testDeleteInvoice_ShouldThrowRecordNotFoundException() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.existsById(invoiceId)).thenReturn(false);

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.deleteInvoice(invoiceId));

        // Then
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).existsById(invoiceId);
        verify(invoiceRepository, never()).deleteById(invoiceId);
        verifyNoMoreInteractions(invoiceRepository);
    }

}
