package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.InvoiceRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.helpers.InvoiceFactoryHelper;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.services.InvoiceService;
import nl.novi.catsittermanager.services.OrderService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllInvoices_thenAllInvoicesShouldBeReturned() throws Exception {

        // Arrange
        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

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

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

        Invoice expectedInvoice = Invoice.builder()
                .invoiceNo(UUID.randomUUID())
                .invoiceDate(LocalDate.now())
                .amount(100.0)
                .paid(false)
                .order(expectedOrder)
                .build();

        List<Invoice> expectedInvoiceList = List.of(expectedInvoice);

        when(invoiceService.getAllInvoices()).thenReturn(expectedInvoiceList);

        InvoiceResponse expectedResponse = new InvoiceResponse(
                expectedInvoice.getInvoiceNo(),
                expectedInvoice.getInvoiceDate().toString(),
                expectedInvoice.getAmount(),
                expectedInvoice.getPaid(),
                expectedInvoice.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedInvoiceList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/invoices")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedInvoiceList.size()))
                .andExpect(jsonPath("$.[0].invoiceNo").value(expectedResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.[0].invoiceDate").value(expectedResponse.invoiceDate()))
                .andExpect(jsonPath("$.[0].amount").value(expectedResponse.amount()))
                .andExpect(jsonPath("$.[0].paid").value(expectedResponse.paid()))
                .andExpect(jsonPath("$.[0].orderNo").value(expectedResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoInvoicesAvailable_whenGetAllInvoices_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        when(invoiceService.getAllInvoices()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/invoices")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetInvoice_thenInvoiceShouldBeReturned() throws Exception {

        // Arrange
        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

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

        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

        Invoice expectedInvoice = Invoice.builder()
                .invoiceNo(UUID.randomUUID())
                .invoiceDate(LocalDate.now())
                .amount(100.0)
                .paid(false)
                .order(expectedOrder)
                .build();

        when(invoiceService.getInvoice(expectedInvoice.getInvoiceNo())).thenReturn(expectedInvoice);

        InvoiceResponse expectedResponse = new InvoiceResponse(
                expectedInvoice.getInvoiceNo(),
                expectedInvoice.getInvoiceDate().toString(),
                expectedInvoice.getAmount(),
                expectedInvoice.getPaid(),
                expectedInvoice.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedInvoice);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/invoice/" + expectedInvoice.getInvoiceNo().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNo").value(expectedResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.invoiceDate").value(expectedResponse.invoiceDate().toString()))
                .andExpect(jsonPath("$.amount").value(expectedResponse.amount()))
                .andExpect(jsonPath("$.paid").value(expectedResponse.paid()))
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidInvoiceNo_whenGetInvoice_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidInvoiceNo = UUID.randomUUID();
        final String errorMessage = "No invoice found with this id.";

        when(invoiceService.getInvoice(invalidInvoiceNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/invoice/" + invalidInvoiceNo)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenCreateInvoice_thenInvoiceShouldBeReturned() throws Exception {

        // Arrange
        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();
        UUID orderNo = expectedInvoiceRequest.orderNo();
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

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

        Order expectedOrder = Order.builder()
                .orderNo(expectedInvoiceRequest.orderNo())
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

        expectedInvoice.setOrder(expectedOrder);

        when(orderService.getOrder(orderNo)).thenReturn(expectedOrder);
        when(invoiceService.createInvoice(any(Invoice.class), eq(expectedInvoiceRequest.orderNo())))
                .thenReturn(expectedInvoice);

        InvoiceResponse expectedResponse = new InvoiceResponse(
                expectedInvoice.getInvoiceNo(),
                expectedInvoice.getInvoiceDate().toString(),
                expectedInvoice.getAmount(),
                expectedInvoice.getPaid(),
                expectedInvoice.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedInvoiceRequest);

        // Act & Assert
        mockMvc.perform(post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNo").value(expectedResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.invoiceDate").value(expectedResponse.invoiceDate()))
                .andExpect(jsonPath("$.amount").value(expectedResponse.amount()))
                .andExpect(jsonPath("$.paid").value(expectedResponse.paid()))
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenExistingInvoiceForGivenOrder_whenCreateInvoice_thenConflictShouldBeReturned() throws Exception {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        InvoiceRequest request = InvoiceRequestFactory.randomInvoiceRequest()
                .invoiceDate(InvoiceFactoryHelper.randomDateIn2024().toString())
                .paid(false)  // setting paid to false as per business logic
                .orderNo(orderNo)
                .build();

        Order expectedOrder = Order.builder()
                .orderNo(orderNo)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(new ArrayList<>())
                .build();

        // Mocking orderService to return expectedOrder
        when(orderService.getOrder(orderNo)).thenReturn(expectedOrder);

        // Mocking invoiceService to throw InvoiceAlreadyExistsForThisOrderException
        when(invoiceService.createInvoice(any(Invoice.class), eq(orderNo)))
                .thenThrow(new InvoiceAlreadyExistsForThisOrderException(orderNo));

        String content = objectMapper.writeValueAsString(request);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string("An invoice already exists for order " + orderNo + "."))
                .andReturn();

        Assertions.assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNonExistingOrder_whenCreateInvoice_thenNotFoundShouldBeReturned() throws Exception {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        InvoiceRequest request = InvoiceRequestFactory.randomInvoiceRequest()
                .orderNo(orderNo)
                .build();

        when(invoiceService.createInvoice(any(Invoice.class), eq(orderNo))).thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "Order not found"));

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"))
                .andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateInvoice_thenBadRequest() throws Exception {

        // Arrange
        InvoiceRequest invalidInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest()
                .invoiceDate(null)
                .paid(null)
                .orderNo(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInvoiceRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(invoiceService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditInvoice_thenEditedInvoiceShouldBeReturned() throws Exception {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();

        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        List<Task> tasks = List.of(
                Task.builder().taskNo(UUID.randomUUID()).taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).taskInstruction("Feed the cat").extraInstructions("Use the blue bowl").build(),
                Task.builder().taskNo(UUID.randomUUID()).taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).taskInstruction("Give water to the cat").extraInstructions("Use the red bowl").build()
        );

        Order expectedOrder = Order.builder()
                .orderNo(orderNo)
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

        Invoice expectedInvoice = InvoiceMapper.InvoiceRequestToInvoice(expectedInvoiceRequest, expectedOrder);
        expectedInvoice.setInvoiceNo(invoiceId);

        when(orderService.getOrder(orderNo)).thenReturn(expectedOrder);
        when(invoiceService.editInvoice(any(Invoice.class))).thenReturn(expectedInvoice);

        InvoiceResponse expectedInvoiceResponse = new InvoiceResponse(
                expectedInvoice.getInvoiceNo(),
                expectedInvoice.getInvoiceDate().toString(),
                expectedInvoice.getAmount(),
                expectedInvoice.getPaid(),
                expectedInvoice.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedInvoiceRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/invoice/" + invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNo").value(expectedInvoiceResponse.invoiceNo().toString()))
                .andExpect(jsonPath("$.invoiceDate").value(expectedInvoiceResponse.invoiceDate()))
                .andExpect(jsonPath("$.amount").value(expectedInvoiceResponse.amount()))
                .andExpect(jsonPath("$.paid").value(expectedInvoiceResponse.paid()))
                .andExpect(jsonPath("$.orderNo").value(expectedInvoiceResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditInvoice_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {
        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();
        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        when(orderService.getOrder(orderNo)).thenReturn(null);  // Mocking orderService to return null

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/invoice/{id}", invoiceNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedInvoiceRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidOrderId_whenEditInvoice_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        UUID orderNo = UUID.randomUUID();

        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().orderNo(orderNo).build();

        when(orderService.getOrder(orderNo)).thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id"));

        String content = objectMapper.writeValueAsString(expectedInvoiceRequest);

        // Act & Assert
        mockMvc.perform(put("/api/invoice/{id}", invoiceNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditInvoice_thenBadRequest() throws Exception {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();

        InvoiceRequest invalidInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest()
                .invoiceDate(null)
                .paid(null)
                .orderNo(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/invoice/{id}", invoiceNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInvoiceRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(invoiceService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteInvoice_thenInvoiceShouldBeDeleted() throws Exception {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();

        when(invoiceService.deleteInvoice(invoiceNo)).thenReturn(invoiceNo);

        // Act & Assert
        mockMvc.perform(delete("/api/invoice/{id}", invoiceNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Invoice with id " + invoiceNo + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteInvoice_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidInvoiceNo = UUID.randomUUID();
        final String errorMessage = "No invoice found with this id";

        when(invoiceService.deleteInvoice(invalidInvoiceNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/invoice/{id}", invalidInvoiceNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}
