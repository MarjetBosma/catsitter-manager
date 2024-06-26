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
import nl.novi.catsittermanager.enumerations.TaskType;
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

import java.time.LocalDate;
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

    private List<Task> tasks;

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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        List<Task> tasks = List.of(
                Task.builder()
                        .taskNo(UUID.randomUUID())
                        .taskType(TaskType.FOOD)
                        .priceOfTask(TaskType.FOOD.getPrice())
                        .taskInstruction("Feed the cat")
                        .extraInstructions("Use the blue bowl")
                        .build(),
                Task.builder()
                        .taskNo(UUID.randomUUID())
                        .taskType(TaskType.WATER)
                        .priceOfTask(TaskType.WATER.getPrice())
                        .taskInstruction("Give water to the cat")
                        .extraInstructions("Use the red bowl")
                        .build()
        );


        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

        when(orderService.getAllTasksByOrder(expectedOrder.getOrderNo())).thenReturn(tasks);

        List<TaskResponse> expectedResponses = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getTaskNo(),
                        task.getTaskType(),
                        task.getTaskInstruction(),
                        task.getExtraInstructions(),
                        task.getPriceOfTask(),
                        task.getOrder().getOrderNo()
                )).toList();

        String content = objectMapper.writeValueAsString(tasks);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/order/" + expectedOrder.getOrderNo() + "/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tasks.size()))
                .andExpect(jsonPath("$.[0].taskNo").value(expectedResponses.get(0).taskNo().toString()))
                .andExpect(jsonPath("$.[0].taskType").value(expectedResponses.get(0).taskType().toString()))
                .andExpect(jsonPath("$.[0].taskInstruction").value(expectedResponses.get(0).taskInstruction()))
                .andExpect(jsonPath("$.[0].extraInstructions").value(expectedResponses.get(0).extraInstructions()))
                .andExpect(jsonPath("$.[0].priceOfTask").value(expectedResponses.get(0).priceOfTask()))
                .andExpect(jsonPath("$.[0].orderNo").value(expectedResponses.get(0).orderNo().toString()))
                .andExpect(jsonPath("$.[1].taskNo").value(expectedResponses.get(1).taskNo().toString()))
                .andExpect(jsonPath("$.[1].taskType").value(expectedResponses.get(1).taskType().toString()))
                .andExpect(jsonPath("$.[1].taskInstruction").value(expectedResponses.get(1).taskInstruction()))
                .andExpect(jsonPath("$.[1].extraInstructions").value(expectedResponses.get(1).extraInstructions()))
                .andExpect(jsonPath("$.[1].priceOfTask").value(expectedResponses.get(1).priceOfTask()))
                .andExpect(jsonPath("$.[1].orderNo").value(expectedResponses.get(1).orderNo().toString()));
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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

        Invoice invoice = Invoice.builder()
                .invoiceNo(UUID.randomUUID())
                .invoiceDate(LocalDate.now())
                .amount(expectedOrder.calculateTotalCost())
                .paid(false)
                .order(expectedOrder)
                .build();

        InvoiceResponse invoiceResponse = new InvoiceResponse(
                invoice.getInvoiceNo(),
                invoice.getInvoiceDate().toString(),
                invoice.getAmount(),
                invoice.getPaid(),
                invoice.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(tasks);
        System.out.println(content);

        when(orderService.getInvoiceByOrder(expectedOrder.getOrderNo())).thenReturn(invoice);

        // Act & Assert

        mockMvc.perform(get("/api/order/" + expectedOrder.getOrderNo() + "/invoice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.invoiceNo").value(invoiceResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.invoiceDate").value(invoiceResponse.invoiceDate()))
                .andExpect(jsonPath("$.amount").value(invoiceResponse.amount()))
                .andExpect(jsonPath("$.paid").value(invoiceResponse.paid()))
                .andExpect(jsonPath("$.orderNo").value(invoiceResponse.orderNo().toString()));

        // Verify
        verify(orderService, times(1)).getInvoiceByOrder(expectedOrder.getOrderNo());
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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        OrderRequest expectedOrderRequest = OrderRequest.builder()
                .startDate("2024-06-26")
                .endDate("2024-06-30")
                .dailyNumberOfVisits(2)
                .customerUsername("customerUsername")
                .catsitterUsername("catsitterUsername")
                .build();

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.parse(expectedOrderRequest.startDate()))
                .endDate(LocalDate.parse(expectedOrderRequest.endDate()))
                .dailyNumberOfVisits(expectedOrderRequest.dailyNumberOfVisits())
                .totalNumberOfVisits(10) // Example calculation
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

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
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        OrderRequest expectedOrderRequest = OrderRequest.builder()
                .startDate("2024-06-26")
                .endDate("2024-06-30")
                .dailyNumberOfVisits(2)
                .customerUsername("customerUsername")
                .catsitterUsername("catsitterUsername")
                .build();

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.parse(expectedOrderRequest.startDate()))
                .endDate(LocalDate.parse(expectedOrderRequest.endDate()))
                .dailyNumberOfVisits(expectedOrderRequest.dailyNumberOfVisits())
                .totalNumberOfVisits(10)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

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

        when(orderService.editOrder(any(UUID.class), any(Order.class), eq(expectedOrderRequest.customerUsername()), eq(expectedOrderRequest.catsitterUsername())))
                .thenReturn(expectedOrder);

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
