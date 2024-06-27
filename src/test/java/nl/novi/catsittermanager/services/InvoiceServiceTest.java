package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.InvoiceRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = OrderFactory.randomOrder(tasks).build();
        expectedOrder.setOrderNo(expectedInvoiceRequest.orderNo());
        expectedOrder.setStartDate(LocalDate.now());
        expectedOrder.setEndDate(LocalDate.now().plusDays(5));
        expectedOrder.setDailyNumberOfVisits(2);
        expectedOrder.setTasks(tasks);

        double expectedAmount = expectedOrder.calculateTotalCost();

        Invoice expectedInvoice = InvoiceMapper.InvoiceRequestToInvoice(expectedInvoiceRequest, expectedOrder);

        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);
        when(orderService.hasExistingInvoice(expectedOrder.getOrderNo())).thenReturn(false);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(expectedInvoice);

        Invoice resultInvoice = invoiceService.createInvoice(expectedInvoice, expectedInvoice.getOrder().getOrderNo());

        // Assert
        assertNotNull(resultInvoice);
        assertEquals(expectedInvoice.getInvoiceNo(), resultInvoice.getInvoiceNo());
        assertEquals(expectedInvoice.getInvoiceDate(), resultInvoice.getInvoiceDate());
        assertEquals(expectedAmount, resultInvoice.getAmount());
        assertEquals(expectedInvoice.getPaid(), resultInvoice.getPaid());
        assertEquals(expectedOrder, resultInvoice.getOrder());

        verify(orderService, times(1)).getOrder(expectedOrder.getOrderNo());
        verify(orderService, times(1)).hasExistingInvoice(expectedOrder.getOrderNo());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testCreateInvoice_invoiceAlreadyExists() {
        // Arrange
        UUID orderNo = UUID.randomUUID();
        InvoiceRequest invoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order existingOrder = OrderFactory.randomOrder(tasks).build();
        existingOrder.setOrderNo(orderNo);
        existingOrder.setOrderNo(invoiceRequest.orderNo());
        existingOrder.setStartDate(LocalDate.now());
        existingOrder.setEndDate(LocalDate.now().plusDays(5));
        existingOrder.setDailyNumberOfVisits(2);
        existingOrder.setTasks(tasks);

        Invoice mappedInvoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest, existingOrder);
        mappedInvoice.setOrder(existingOrder);

        when(orderService.getOrder(existingOrder.getOrderNo())).thenReturn(existingOrder);
        when(orderService.hasExistingInvoice(orderNo)).thenReturn(true);

        // Act & Assert
        assertThrows(InvoiceAlreadyExistsForThisOrderException.class, () -> {
            invoiceService.createInvoice(mappedInvoice, orderNo);
        });

        verify(orderService, times(1)).getOrder(orderNo);
        verify(orderService, times(1)).hasExistingInvoice(orderNo);
        verifyNoInteractions(invoiceRepository);
    }

    @Test
    void testCreateInvoice_orderNotFound() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        InvoiceRequest invoiceRequest = new InvoiceRequest(
                "2024-06-27",
                100.0,
                false,
                orderNo);

        when(orderService.getOrder(orderNo)).thenReturn(null);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.createInvoice(InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest, null), orderNo);
        });

        assertEquals("Order not found", exception.getMessage());

        // Verify interactions
        verify(orderService, times(1)).getOrder(orderNo);
        verifyNoInteractions(invoiceRepository);
    }

    @Test
    void testEditInvoice_withOrder_shouldEditInvoice() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order existingOrder = OrderFactory.randomOrder(tasks).build();
        existingOrder.setOrderNo(orderNo);

        Invoice existingInvoice = InvoiceFactory.randomInvoice().order(existingOrder).build();
        existingInvoice.setInvoiceNo(invoiceId);

        InvoiceRequest updatedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(existingInvoice);

        Invoice updatedInvoice = InvoiceMapper.InvoiceRequestToInvoice(updatedInvoiceRequest, existingOrder);
        updatedInvoice.setInvoiceNo(invoiceId);

        // Act
        Invoice resultInvoice = invoiceService.editInvoice(updatedInvoice);

        // Assert
        assertNotNull(resultInvoice);
        assertEquals(updatedInvoiceRequest.invoiceDate(), resultInvoice.getInvoiceDate().toString());
        assertEquals(updatedInvoiceRequest.paid(), resultInvoice.getPaid());
        assertEquals(existingOrder, resultInvoice.getOrder());

        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));

    }

    @Test
    void testEditInvoice_nonExistingInvoice_shouldThrowRecordNotFoundException() {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();

        InvoiceRequest updatedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        when(invoiceRepository.findById(invoiceNo)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.getInvoice(invoiceNo);
        });

        // Assert
        verifyNoMoreInteractions(invoiceRepository);
        verifyNoInteractions(orderService);
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
