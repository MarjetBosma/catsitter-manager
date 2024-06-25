package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // Arrange
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        List<Invoice> expectedInvoiceList = List.of(expectedInvoice);

        when(invoiceRepository.findAll()).thenReturn(expectedInvoiceList);

        // Act
        List<Invoice> actualInvoiceList = invoiceService.getAllInvoices();

        // Assert
        assertEquals(expectedInvoiceList, actualInvoiceList);
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void testGetAllInvoices_noInvoicesInDatabase_shouldReturnEmptyList() {

        // Arrange
        when(invoiceRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Invoice> invoiceResponseList = invoiceService.getAllInvoices();

        // Assert
        assertTrue(invoiceResponseList.isEmpty());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void testGetInvoice_shouldFetchInvoiceWithSpecificId() {

        // Arrange
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        when(invoiceRepository.findById(expectedInvoice.getInvoiceNo())).thenReturn(Optional.of(expectedInvoice));

        // Act
        Invoice resultInvoice = invoiceService.getInvoice(expectedInvoice.getInvoiceNo());

        // Assert
        assertEquals(expectedInvoice, resultInvoice);
        verify(invoiceRepository, times(1)).findById(expectedInvoice.getInvoiceNo());
    }

    @Test
    void testGetInvoice_shouldFetchInvoiceWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.getInvoice(invoiceId));

        // Assert
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository).findById(invoiceId);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    void testCreateInvoice_shouldCreateANewInvoice() {

        // Arrange
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );
        Order expectedOrder = OrderFactory.randomOrder(tasks).build();

        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);
        when(orderService.hasExistingInvoice(expectedOrder.getOrderNo())).thenReturn(false);
        when(invoiceRepository.save(expectedInvoice)).thenReturn(expectedInvoice);

        // Act
        Invoice resultInvoice = invoiceService.createInvoice(expectedInvoice, expectedOrder.getOrderNo());

        // Assert
        assertEquals(expectedInvoice, resultInvoice);
        assertEquals(expectedOrder, resultInvoice.getOrder());

        verify(orderService, times(1)).getOrder(expectedOrder.getOrderNo());
        verify(invoiceRepository, times(1)).save(expectedInvoice);
    }

    @Test
    void testCreateInvoice_invoiceAlreadyExists() {
        // Arrange
        UUID orderNo = UUID.randomUUID();
        Order order = new Order();
        Invoice invoice = new Invoice();

        when(orderService.getOrder(orderNo)).thenReturn(order);
        when(orderService.hasExistingInvoice(orderNo)).thenReturn(true);

        // Act & Assert
        assertThrows(InvoiceAlreadyExistsForThisOrderException.class, () -> invoiceService.createInvoice(invoice, orderNo));

        verify(orderService, times(1)).getOrder(orderNo);
        verify(orderService, times(1)).hasExistingInvoice(orderNo);
        verifyNoInteractions(invoiceRepository);
    }

    @Test
    void testCreateInvoice_orderNotFound() {
        // Arrange
        UUID orderNo = UUID.randomUUID();
        Invoice invoice = new Invoice();
        when(orderService.getOrder(orderNo)).thenThrow(new RecordNotFoundException("No order found with this id"));

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> invoiceService.createInvoice(invoice, orderNo));

        verify(orderService, times(1)).getOrder(orderNo);
        verifyNoInteractions(invoiceRepository);
    }

    @Test
    void testEditInvoice_withOrder_shouldEditInvoiceWithOrderPresent() {
        // Arrange
        Invoice invoice = InvoiceFactory.randomInvoice().build();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = OrderFactory.randomOrder(tasks).build();
        invoice.setOrder(expectedOrder);

        when(invoiceRepository.findById(invoice.getInvoiceNo())).thenReturn(Optional.of(invoice));
        when(orderService.getOrder(invoice.getOrder().getOrderNo())).thenReturn(expectedOrder);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        // Act
        Invoice resultInvoice = invoiceService.editInvoice(invoice.getInvoiceNo(), invoice, invoice.getOrder().getOrderNo());

        // Assert
        assertEquals(invoice, resultInvoice);
        assertEquals(expectedOrder, resultInvoice.getOrder());

        verify(invoiceRepository, times(1)).findById(invoice.getInvoiceNo());
        verify(orderService, times(1)).getOrder(invoice.getOrder().getOrderNo());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testEditInvoice_nonExistingInvoice_shouldThrowRecordNotFoundException() {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        Invoice invoice = InvoiceFactory.randomInvoice().build();
        UUID orderNo = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceNo)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.editInvoice(invoiceNo, invoice, orderNo));

        // Assert
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(invoiceNo);
    }

    @Test
    void testDeleteInvoice_ShouldDeleteInvoiceWithSpecificId() {

        // Arrange
        Invoice invoice = InvoiceFactory.randomInvoice().build();
        when(invoiceRepository.existsById(invoice.getInvoiceNo())).thenReturn(true);

        // Act
        UUID invoiceNo = invoiceService.deleteInvoice(invoice.getInvoiceNo());

        // Assert
        verify(invoiceRepository, times(1)).existsById(invoiceNo);
        verify(invoiceRepository, times(1)).deleteById(invoiceNo);
        verifyNoInteractions(orderService);
    }

    @Test
    void testDeleteInvoice_ShouldThrowRecordNotFoundException() {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        when(invoiceRepository.existsById(invoiceNo)).thenReturn(false);

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> invoiceService.deleteInvoice(invoiceNo));

        // Assert
        assertEquals("No invoice found with this id.", exception.getMessage());
        verify(invoiceRepository, times(1)).existsById(invoiceNo);
        verify(invoiceRepository, never()).deleteById(invoiceNo);
        verifyNoInteractions(orderService);
    }
}
