package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
        // Given
        Order expectedOrder = OrderFactory.randomOrder().build();
        List<Order> expectedOrderList = List.of(expectedOrder);

        when(orderRepository.findAll()).thenReturn(expectedOrderList);

        // When
        List<Order> catsitterResponseList = orderService.getAllOrders();

        // Then
        assertEquals(expectedOrderList, catsitterResponseList);

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_noOrdersInDatabase_shouldReturnEmptyList() {
        // Given
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Order> orderResponseList = orderService.getAllOrders();

        // Then
        assertTrue(orderResponseList.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrder_shouldFetchOrderWithSpecificOrderNo() {
        // Given
        Order expectedOrder = OrderFactory.randomOrder().build();

        when(orderRepository.findById(expectedOrder.getOrderNo())).thenReturn(Optional.of(expectedOrder));

        // When
        Order resultOrder = orderService.getOrder(expectedOrder.getOrderNo());

        // Then
        assertEquals(expectedOrder, resultOrder);
        verify(orderRepository, times(1)).findById(expectedOrder.getOrderNo());
    }

    @Test
    void testGetOrder_shouldFetchOrderWithSpecificOrderNo_RecordNotFoundException() {
        // Given
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.getOrder(orderNo));

        // Then
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository).findById(orderNo);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testGetAllTasksByOrder_shouldFetchAllTasksForThisOrder() {
        // Given
        UUID orderId = UUID.randomUUID();
        Order order = OrderFactory.randomOrder().orderNo(orderId).build();

        Task task1 = new Task();
        task1.setTaskNo(UUID.randomUUID());
        task1.setTaskType(TaskType.valueOf("FOOD"));

        Task task2 = new Task();
        task2.setTaskNo(UUID.randomUUID());
        task2.setTaskType(TaskType.valueOf("WATER"));

        Task task3 = new Task();
        task2.setTaskNo(UUID.randomUUID());
        task2.setTaskType(TaskType.valueOf("LITTERBOX"));

        List<Task> expectedTasks = List.of(task1, task2, task3);
        order.setTasks(expectedTasks);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        List<Task> resultTasks = orderService.getAllTasksByOrder(orderId);

        // Then
        assertEquals(expectedTasks.size(), resultTasks.size());
        assertTrue(resultTasks.containsAll(expectedTasks));

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetAllTasksByOrder_noTasksOnTheList_shouldReturnEmptyList() {
        // Given
        Order randomOrder = OrderFactory.randomOrder().build();
        randomOrder.setTasks(Collections.emptyList());

        when(orderRepository.findById(randomOrder.getOrderNo())).thenReturn(Optional.of(randomOrder));

        // When
        List<Task> resultTasks = orderService.getAllTasksByOrder(randomOrder.getOrderNo());

        // Then
        assertNotNull(resultTasks);
        assertTrue(resultTasks.isEmpty());

        verify(orderRepository, times(1)).findById(randomOrder.getOrderNo());
    }


    @Test
    void testCreateOrder_shouldCreateANewOrder() {
        // Given
        Order expectedOrder = OrderFactory.randomOrder().build();
        Customer customer = CustomerFactory.randomCustomer().build();
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();

        when(customerService.getCustomer(customer.getUsername())).thenReturn(customer);
        when(catsitterService.getCatsitter(catsitter.getUsername())).thenReturn(catsitter);
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        // When
        Order resultOrder = orderService.createOrder(expectedOrder, customer.getUsername(), catsitter.getUsername());

        // Then
        assertEquals(expectedOrder, resultOrder);

        verify(customerService, times(1)).getCustomer(customer.getUsername());
        verify(catsitterService, times(1)).getCatsitter(catsitter.getUsername());

        verify(orderRepository, times(1)).save(expectedOrder);
    }

    @Test
    void testEditOrder_shouldEditExistingOrder() {
        // Given
        UUID orderId = UUID.randomUUID();
        Order existingOrder = OrderFactory.randomOrder().orderNo(orderId).build();
        Order updatedOrder = OrderFactory.randomOrder().build();
        String customerUsername = updatedOrder.getCustomer().getUsername();
        String catsitterUsername = updatedOrder.getCatsitter().getUsername();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(customerService.getCustomer(customerUsername)).thenReturn(updatedOrder.getCustomer());
        when(catsitterService.getCatsitter(catsitterUsername)).thenReturn(updatedOrder.getCatsitter());
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // When
        Order resultOrder = orderService.editOrder(orderId, updatedOrder, customerUsername, catsitterUsername);

        // Then
        assertEquals(existingOrder.getOrderNo(), resultOrder.getOrderNo());
        assertEquals(updatedOrder.getStartDate(), resultOrder.getStartDate());
        assertEquals(updatedOrder.getEndDate(), resultOrder.getEndDate());
        assertEquals(updatedOrder.getDailyNumberOfVisits(), resultOrder.getDailyNumberOfVisits());
        assertEquals(updatedOrder.getTotalNumberOfVisits(), resultOrder.getTotalNumberOfVisits());
        assertEquals(updatedOrder.getCustomer().getUsername(), resultOrder.getCustomer().getUsername());
        assertEquals(updatedOrder.getCatsitter().getUsername(), resultOrder.getCatsitter().getUsername());

        verify(orderRepository, times(1)).findById(orderId);
        verify(customerService, times(1)).getCustomer(customerUsername);
        verify(catsitterService, times(1)).getCatsitter(catsitterUsername);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testEditOrder_nonExistingOrder_shouldThrowRecordNotFoundException() {
        // Given
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.getOrder(orderNo));

        // Then
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository).findById(orderNo);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testDeleteOrder_ShouldDeleteOrderWithSpecificId() {
        // Given
        Order order = OrderFactory.randomOrder().build();
        when(orderRepository.existsById(order.getOrderNo())).thenReturn(true);

        // When
        UUID orderNo = orderService.deleteOrder(order.getOrderNo());

        // Then
        verify(orderRepository, times(1)).existsById(orderNo);
        verify(orderRepository, times(1)).deleteById(orderNo);
        verifyNoInteractions(customerService);
        verifyNoInteractions(catsitterService);
    }

    @Test
    void testDeleteOrder_shouldDeleteOrderWithSpecificId_RecordNotFoundException() {
        // Given
        UUID orderNo = UUID.randomUUID();
        when(orderRepository.existsById(orderNo)).thenReturn(false);

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> orderService.deleteOrder(orderNo));

        // Then
        assertEquals("No order found with this id.", exception.getMessage());
        verify(orderRepository, never()).deleteById(orderNo);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(customerService);
        verifyNoInteractions(catsitterService);
    }

}
