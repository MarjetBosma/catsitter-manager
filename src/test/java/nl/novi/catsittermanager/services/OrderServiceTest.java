package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.OrderRepository;
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
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CatsitterService catsitterService;

    @InjectMocks
    OrderService orderService;

    @Test
    void testGetAllOrders_shouldFetchAllOrdersOnTheList() {

        // Arrange
        Order expectedOrder = OrderFactory.randomOrder().build();
        List<Order> expectedOrderList = List.of(expectedOrder);

        when(orderRepository.findAll()).thenReturn(expectedOrderList);

        // Act
        List<Order> orderResponseList = orderService.getAllOrders();

        // Assert
        assertEquals(expectedOrderList, orderResponseList);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_noOrdersInDatabase_shouldReturnEmptyList() {

        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Order> orderResponseList = orderService.getAllOrders();

        // Assert
        assertTrue(orderResponseList.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrder_shouldFetchOrderWithSpecificOrderNo() {

        // Arrange
        Order expectedOrder = OrderFactory.randomOrder().build();
        when(orderRepository.findById(expectedOrder.getOrderNo())).thenReturn(Optional.of(expectedOrder));

        // Act
        Order resultOrder = orderService.getOrder(expectedOrder.getOrderNo());

        // Assert
        assertEquals(expectedOrder, resultOrder);
        verify(orderRepository, times(1)).findById(expectedOrder.getOrderNo());
    }

    @Test
    void testGetOrder_shouldFetchOrderWithSpecificOrderNo_RecordNotFoundException() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.getOrder(orderNo));

        // Assert
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository).findById(orderNo);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testGetAllTasksByOrder_shouldFetchAllTasksForThisOrder() {

        // Arrange
        Order randomOrder = OrderFactory.randomOrder().build();
        List<Task> expectedTasks = TaskFactory.randomTasks(3);
        randomOrder.setTasks(expectedTasks);

        when(orderRepository.findById(randomOrder.getOrderNo())).thenReturn(Optional.of(randomOrder));

        // Act
        List<Task> resultTasks = orderService.getAllTasksByOrder(randomOrder.getOrderNo());

        // Assert
        assertEquals(expectedTasks.size(), resultTasks.size());
        assertTrue(resultTasks.containsAll(expectedTasks));

        verify(orderRepository, times(1)).findById(randomOrder.getOrderNo());
    }

    @Test
    void testGetAllTasksByOrder_noTasksOnTheList_shouldReturnEmptyList() {

        // Arrange
        Order randomOrder = OrderFactory.randomOrder().build();
        randomOrder.setTasks(Collections.emptyList());

        when(orderRepository.findById(randomOrder.getOrderNo())).thenReturn(Optional.of(randomOrder));

        // Act
        List<Task> resultTasks = orderService.getAllTasksByOrder(randomOrder.getOrderNo());

        // Assert
        assertNotNull(resultTasks);
        assertTrue(resultTasks.isEmpty());

        verify(orderRepository, times(1)).findById(randomOrder.getOrderNo());
    }

    @Test
    void getInvoiceByOrder_shouldFetchInvoiceForThisOrder() {

        // Arrange
        Order randomOrder = OrderFactory.randomOrder().build();
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
        randomOrder.setInvoice(expectedInvoice);

        when(orderRepository.findById(randomOrder.getOrderNo())).thenReturn(Optional.of(randomOrder));

        // Act
        Invoice resultInvoice = orderService.getInvoiceByOrder(randomOrder.getOrderNo());

        // Assert
        assertEquals(expectedInvoice, resultInvoice);

        verify(orderRepository, times(1)).findById(randomOrder.getOrderNo());
    }

    @Test
    void testGetInvoiceByOrder_noInvoicePresent_RecordNotFoundException() {

        //  Arrange
        UUID orderNo = UUID.randomUUID();
        Order randomOrder = OrderFactory.randomOrder().build();
        randomOrder.setInvoice(null);

        doReturn(Optional.of(randomOrder)).when(orderRepository).findById(orderNo);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            orderService.getInvoiceByOrder(orderNo);
        });

        assertEquals("No invoice found for this order.", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderNo);
    }

    @Test
    void testCreateOrder_shouldCreateANewOrder() {

        // Arrange
        Order expectedOrder = OrderFactory.randomOrder().build();
        Customer customer = CustomerFactory.randomCustomer().build();
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();

        when(customerService.getCustomer(customer.getUsername())).thenReturn(customer);
        when(catsitterService.getCatsitter(catsitter.getUsername())).thenReturn(catsitter);
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        // Act
        Order resultOrder = orderService.createOrder(expectedOrder, customer.getUsername(), catsitter.getUsername());

        // Assert
        assertEquals(expectedOrder, resultOrder);

        verify(customerService, times(1)).getCustomer(customer.getUsername());
        verify(catsitterService, times(1)).getCatsitter(catsitter.getUsername());
        verify(orderRepository, times(1)).save(expectedOrder);
    }

    @Test
    void testEditOrder_shouldEditExistingOrder() {

        // Arrange
        Order order = OrderFactory.randomOrder().build();

        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.of(order));
        when(customerService.getCustomer(order.getCustomer().getUsername())).thenReturn(order.getCustomer());
        when(catsitterService.getCatsitter(order.getCatsitter().getUsername())).thenReturn(order.getCatsitter());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order resultOrder = orderService.editOrder(order.getOrderNo(), order, order.getCustomer().getUsername(), order.getCatsitter().getUsername());

        // Assert
        assertEquals(order, resultOrder);

        verify(orderRepository, times(1)).findById(order.getOrderNo());
        verify(customerService, times(1)).getCustomer(order.getCustomer().getUsername());
        verify(catsitterService, times(1)).getCatsitter(order.getCatsitter().getUsername());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testEditOrder_withCatsitter_shouldEditOrderWithCatsitterPresent() {

        // Arrange
        Order order = OrderFactory.randomOrder().build();
        Catsitter newCatsitter = CatsitterFactory.randomCatsitter().build();
        order.setCatsitter(newCatsitter);

        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.of(order));
        when(catsitterService.getCatsitter(newCatsitter.getUsername())).thenReturn(newCatsitter);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order resultOrder = orderService.editOrder(order.getOrderNo(), order, order.getCustomer().getUsername(), newCatsitter.getUsername());

        // Assert
        assertEquals(newCatsitter, resultOrder.getCatsitter());
        verify(orderRepository, times(1)).findById(order.getOrderNo());
        verify(catsitterService, times(1)).getCatsitter(newCatsitter.getUsername());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testEditOrder_withCustomer_shouldEditOrderWithCustomerPresent() {

        // Arrange
        Order order = OrderFactory.randomOrder().build();
        Customer newCustomer = CustomerFactory.randomCustomer().build();
        order.setCustomer(newCustomer);

        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.of(order));
        when(customerService.getCustomer(newCustomer.getUsername())).thenReturn(newCustomer);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order resultOrder = orderService.editOrder(order.getOrderNo(), order, newCustomer.getUsername(), order.getCatsitter().getUsername());

        // Assert
        assertEquals(newCustomer, resultOrder.getCustomer());
        verify(orderRepository, times(1)).findById(order.getOrderNo());
        verify(customerService, times(1)).getCustomer(newCustomer.getUsername());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testEditOrder_nonExistingOrder_shouldThrowRecordNotFoundException() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.getOrder(orderNo));

        // Assert
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderNo);
    }

    @Test
    void testDeleteOrder_ShouldDeleteOrderWithSpecificId() {

        // Arrange
        Order order = OrderFactory.randomOrder().build();
        when(orderRepository.existsById(order.getOrderNo())).thenReturn(true);

        // Act
        UUID orderNo = orderService.deleteOrder(order.getOrderNo());

        // Assert
        verify(orderRepository, times(1)).existsById(orderNo);
        verify(orderRepository, times(1)).deleteById(orderNo);
        verifyNoInteractions(customerService);
        verifyNoInteractions(catsitterService);
    }

    @Test
    void testDeleteOrder_shouldDeleteOrderWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.existsById(orderNo)).thenReturn(false);

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.deleteOrder(orderNo));

        // Assert
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository, never()).deleteById(orderNo);
        verifyNoInteractions(customerService);
        verifyNoInteractions(catsitterService);
    }

    @Test
    void testHasExistingInvoice_shouldReturnTrueIfOrderHasAnInvoice() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        Order order = new Order();
        Invoice invoice = new Invoice();
        order.setInvoice(invoice);

        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(order));

        // Act
        boolean result = orderService.hasExistingInvoice(orderNo);

        // Assert
        assertTrue(result);
        verify(orderRepository, times(1)).findById(orderNo);
    }

    @Test
    void testHasExistingInvoice_shouldReturnFalseIfOrderHasNoInvoice() {
        // Arrange
        UUID orderNo = UUID.randomUUID();
        Order order = new Order();

        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(order));

        // Act
        boolean result = orderService.hasExistingInvoice(orderNo);

        // Assert
        assertFalse(result);
        verify(orderRepository, times(1)).findById(orderNo);
    }

    @Test
    void testHasExistingInvoice_orderNotFound() {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // Act
        boolean result = orderService.hasExistingInvoice(orderNo);

        // Assert
        assertFalse(result);
        verify(orderRepository, times(1)).findById(orderNo);
    }
}
