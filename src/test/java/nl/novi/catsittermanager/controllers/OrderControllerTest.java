package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.OrderRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.services.OrderService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllOrders_thenAllOrdersShouldBeReturned() throws Exception {
        // Arrange
        Order expectedOrder = OrderFactory.randomOrder().build();
        List<Order> expectedOrderList = List.of(expectedOrder);

        when(orderService.getAllOrders()).thenReturn(expectedOrderList);

        OrderResponse expectedResponse = new OrderResponse(
                expectedOrder.getOrderNo(),
                expectedOrder.getStartDate().toString(),
                expectedOrder.getEndDate().toString(),
                expectedOrder.getDailyNumberOfVisits(),
                expectedOrder.getTotalNumberOfVisits(),
                expectedOrder.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList(),
                expectedOrder.getCustomer().getUsername(),
                expectedOrder.getCatsitter().getUsername(),
                expectedOrder.getInvoice()
        );

        String content = objectMapper.writeValueAsString(expectedOrderList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedOrderList.size()))
                .andExpect(jsonPath("$.[0].orderNo").value(expectedResponse.orderNo().toString()))
                .andExpect(jsonPath("$.[0].startDate").value(expectedResponse.startDate()))
                .andExpect(jsonPath("$.[0].endDate").value(expectedResponse.endDate()))
                .andExpect(jsonPath("$.[0].dailyNumberOfVisits").value(expectedResponse.dailyNumberOfVisits()))
                .andExpect(jsonPath("$.[0].totalNumberOfVisits").value(expectedResponse.totalNumberOfVisits()))
                .andExpect(jsonPath("$.[0].customerUsername").value(expectedResponse.customerUsername()))
                .andExpect(jsonPath("$.[0].catsitterUsername").value(expectedResponse.catsitterUsername()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoOrdersAvailable_whenGetAllOrders_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetOrder_thenOrderShouldBeReturned() throws Exception {

        // Arrange
        Order expectedOrder = OrderFactory.randomOrder().build();

        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);

        OrderResponse expectedResponse = new OrderResponse(
                expectedOrder.getOrderNo(),
                expectedOrder.getStartDate().toString(),
                expectedOrder.getEndDate().toString(),
                expectedOrder.getDailyNumberOfVisits(),
                expectedOrder.getTotalNumberOfVisits(),
                expectedOrder.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList(),
                expectedOrder.getCustomer().getUsername(),
                expectedOrder.getCatsitter().getUsername(),
                expectedOrder.getInvoice()
        );

        String content = objectMapper.writeValueAsString(expectedOrder);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/order/" + expectedOrder.getOrderNo().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()))
                .andExpect(jsonPath("$.startDate").value(expectedResponse.startDate()))
                .andExpect(jsonPath("$.endDate").value(expectedResponse.endDate()))
                .andExpect(jsonPath("$.dailyNumberOfVisits").value(expectedResponse.dailyNumberOfVisits()))
                .andExpect(jsonPath("$.totalNumberOfVisits").value(expectedResponse.totalNumberOfVisits()))
                .andExpect(jsonPath("$.customerUsername").value(expectedResponse.customerUsername()))
                .andExpect(jsonPath("$.catsitterUsername").value(expectedResponse.catsitterUsername()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidOrderNo_whenGetOrder_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidOrderNo = UUID.randomUUID();
        final String errorMessage = "No order found with this id.";

        when(orderService.getOrder(invalidOrderNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/order/" + invalidOrderNo)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllTasksByOrder_thenAllTasksShouldBeReturned() throws Exception {

        // Arrange
        UUID validOrderId = UUID.randomUUID();
        Task expectedTask = TaskFactory.randomTask().build();
        List<Task> expectedTaskList = List.of(expectedTask);

        when(orderService.getAllTasksByOrder(validOrderId)).thenReturn(expectedTaskList);

        TaskResponse expectedResponse = new TaskResponse(
                expectedTask.getTaskNo(),
                expectedTask.getTaskType(),
                expectedTask.getTaskInstruction(),
                expectedTask.getExtraInstructions(),
                expectedTask.getPriceOfTask(),
                expectedTask.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedTaskList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/order/" + validOrderId + "/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedTaskList.size()))
                .andExpect(jsonPath("$.[0].taskNo").value(expectedResponse.taskNo().toString()))
                .andExpect(jsonPath("$.[0].taskType").value(expectedResponse.taskType().toString()))
                .andExpect(jsonPath("$.[0].taskInstruction").value(expectedResponse.taskInstruction()))
                .andExpect(jsonPath("$.[0].extraInstructions").value(expectedResponse.extraInstructions()))
                .andExpect(jsonPath("$.[0].priceOfTask").value(expectedResponse.priceOfTask()))
                .andExpect(jsonPath("$.[0].orderNo").value(expectedResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoTasksAvailableForASpecificOrder_whenGetAllTasksByOrder_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        UUID orderId = UUID.randomUUID();

        when(orderService.getAllTasksByOrder(orderId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/order/" + orderId + "/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetInvoiceByOrder_thenInvoiceShouldBeReturned() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

        when(orderService.getInvoiceByOrder(orderId)).thenReturn(expectedInvoice);

        // Act & Assert
        InvoiceResponse expectedInvoiceResponse = new InvoiceResponse(
                expectedInvoice.getInvoiceNo(),
                expectedInvoice.getInvoiceDate().toString(),
                expectedInvoice.getAmount(),
                expectedInvoice.getPaid(),
                expectedInvoice.getOrder().getOrderNo()
        );

        mockMvc.perform(get("/api/order/" + orderId + "/invoice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.invoiceNo").value(expectedInvoiceResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.invoiceDate").value(expectedInvoiceResponse.invoiceDate()))
                .andExpect(jsonPath("$.amount").value(expectedInvoiceResponse.amount()))
                .andExpect(jsonPath("$.paid").value(expectedInvoiceResponse.paid()))
                .andExpect(jsonPath("$.orderNo").value(expectedInvoiceResponse.orderNo().toString()));

        // Verify
        verify(orderService, times(1)).getInvoiceByOrder(orderId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenARequestWithNoInvoice_whenGetInvoiceByOrder_thenNotFoundShouldBeReturned() throws Exception {
        
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderService.getInvoiceByOrder(orderId)).thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found for this order."));

        // Act & Assert
        mockMvc.perform(get("/api/order/" + orderId + "/invoice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        verify(orderService, times(1)).getInvoiceByOrder(orderId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenCreateOrder_thenOrderShouldBeReturned() throws Exception {

        // Arrange
        OrderRequest expectedOrderRequest = OrderRequestFactory.randomOrderRequest().build();
        Order expectedOrder = OrderFactory.randomOrder().build();

        when(orderService.createOrder(any(Order.class), eq(expectedOrderRequest.customerUsername()), eq(expectedOrderRequest.catsitterUsername())))
                .thenReturn(expectedOrder);

        OrderResponse expectedResponse = new OrderResponse(
                expectedOrder.getOrderNo(),
                expectedOrder.getStartDate().toString(),
                expectedOrder.getEndDate().toString(),
                expectedOrder.getDailyNumberOfVisits(),
                expectedOrder.getTotalNumberOfVisits(),
                expectedOrder.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList(),
                expectedOrder.getCustomer().getUsername(),
                expectedOrder.getCatsitter().getUsername(),
                expectedOrder.getInvoice()
        );

        String content = objectMapper.writeValueAsString(expectedOrderRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()))
                .andExpect(jsonPath("$.startDate").value(expectedResponse.startDate()))
                .andExpect(jsonPath("$.endDate").value(expectedResponse.endDate()))
                .andExpect(jsonPath("$.dailyNumberOfVisits").value(expectedResponse.dailyNumberOfVisits()))
                .andExpect(jsonPath("$.totalNumberOfVisits").value(expectedResponse.totalNumberOfVisits()))
                .andExpect(jsonPath("$.customerUsername").value(expectedResponse.customerUsername()))
                .andExpect(jsonPath("$.catsitterUsername").value(expectedResponse.catsitterUsername()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateOrder_thenBadRequest() throws Exception {

        // Arrange
        OrderRequest invalidOrderRequest = OrderRequestFactory.randomOrderRequest()
                .startDate(null)
                .endDate(null)
                .dailyNumberOfVisits(0)
                .totalNumberOfVisits(0)
                .customerUsername(null)
                .catsitterUsername(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditOrder_thenEditedOrderShouldBeReturned() throws Exception {

        // Arrange
        OrderRequest expectedOrderRequest = OrderRequestFactory.randomOrderRequest().build();
        Order expectedOrder = OrderFactory.randomOrder().build();

        when(orderService.editOrder(any(UUID.class), any(Order.class), eq(expectedOrderRequest.customerUsername()), eq(expectedOrderRequest.catsitterUsername())))
                .thenReturn(expectedOrder);

        OrderResponse expectedResponse = new OrderResponse(
                expectedOrder.getOrderNo(),
                expectedOrder.getStartDate().toString(),
                expectedOrder.getEndDate().toString(),
                expectedOrder.getDailyNumberOfVisits(),
                expectedOrder.getTotalNumberOfVisits(),
                expectedOrder.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList(),
                expectedOrder.getCustomer().getUsername(),
                expectedOrder.getCatsitter().getUsername(),
                expectedOrder.getInvoice()
        );

        String content = objectMapper.writeValueAsString(expectedOrderRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/order/{id}", expectedOrder.getOrderNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()))
                .andExpect(jsonPath("$.startDate").value(expectedResponse.startDate()))
                .andExpect(jsonPath("$.endDate").value(expectedResponse.endDate()))
                .andExpect(jsonPath("$.dailyNumberOfVisits").value(expectedResponse.dailyNumberOfVisits()))
                .andExpect(jsonPath("$.totalNumberOfVisits").value(expectedResponse.totalNumberOfVisits()))
                .andExpect(jsonPath("$.customerUsername").value(expectedResponse.customerUsername()))
                .andExpect(jsonPath("$.catsitterUsername").value(expectedResponse.catsitterUsername()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditOrder_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidOrderNo = UUID.randomUUID();
        OrderRequest expectedOrderRequest = OrderRequestFactory.randomOrderRequest().build();

        when(orderService.editOrder(any(UUID.class), any(Order.class), eq(expectedOrderRequest.customerUsername()), eq(expectedOrderRequest.catsitterUsername())))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id."));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/{id}", invalidOrderNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedOrderRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditOrder_thenBadRequest() throws Exception {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        OrderRequest invalidOrderRequest = OrderRequestFactory.randomOrderRequest()
                .startDate(null)
                .endDate(null)
                .dailyNumberOfVisits(0)
                .totalNumberOfVisits(0)
                .customerUsername(null)
                .catsitterUsername(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/{id}", orderNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteOrder_thenOrderShouldBeDeleted() throws Exception {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        when(orderService.deleteOrder(orderNo)).thenReturn(orderNo);

        // Act & Assert
        mockMvc.perform(delete("/api/order/{id}", orderNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Order with id " + orderNo + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteOrder_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidOrderNo = UUID.randomUUID();
        final String errorMessage = "No order found with this id";

        when(orderService.deleteOrder(invalidOrderNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/order/{id}", invalidOrderNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}
