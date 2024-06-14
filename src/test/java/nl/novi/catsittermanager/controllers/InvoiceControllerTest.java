package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.datafaker.Faker;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.InvoiceRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.helpers.InvoiceFactoryHelper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.InvoiceFactory;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.services.InvoiceService;
import nl.novi.catsittermanager.services.OrderService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.valueOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    @Autowired
    private OrderRepository orderRepository;

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
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();
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
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

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
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

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
        System.out.println(content);

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

    // todo: Geeft een statuscode 201 i.p.v. 409. In Postman werkt dit wel correct, en ook alle unittests voor createInvoice() slagen. Probleem lijkt te zitten in de hasExistingInvoice() methode (uit de OrderService). Unittests hiervan slagen, maar ik heb een SpringBootTest hiervoor geschreven (OrderHasExistingInvoiceIntegrationTest) die vooralsnog ook faalt.
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenExistingInvoiceForGivenOrder_whenCreateInvoice_thenConflictShouldBeReturned() throws Exception {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        System.out.println("orderNo after initialization: " + orderNo); // as expected
        Order orderWithInvoice = new Order();
        orderWithInvoice.setOrderNo(orderNo); // as expected
        System.out.println("orderNo after setting: " + orderNo); // as expected
        orderWithInvoice.setInvoice(new Invoice());

        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(orderWithInvoice));
        when(orderService.hasExistingInvoice(orderNo)).thenReturn(true);  // stelt dat er een invoice aanwezig voor dit ordernummer
        boolean exists = orderService.hasExistingInvoice(orderNo);
        System.out.println(valueOf(exists)); // true

        Invoice mockInvoice = InvoiceFactory.randomInvoice()
                .invoiceNo(UUID.randomUUID())
                .invoiceDate(LocalDate.now())
                .amount(100.0)
                .paid(false)
                .order(orderWithInvoice)
                .build();

        System.out.println("orderNo of mockInvoice: " + mockInvoice.getOrder().getOrderNo()); // as expected
        when(invoiceService.createInvoice(any(Invoice.class), eq(orderNo))).thenReturn(mockInvoice); // zou niet aangeroepen moeten worden i.v.m. conflict status


        Faker faker = new Faker();
        InvoiceRequest request = InvoiceRequestFactory.randomInvoiceRequest() // nieuw request...
                .invoiceDate(InvoiceFactoryHelper.randomDateIn2024().toString())
                .amount(faker.number().randomDouble(2, 50, 300))
                .paid(faker.bool().bool())
                .orderNo(orderNo) // ...voor ordernummer dat al een invoice heeft
                .build();
        System.out.println("orderNo request: " + request.orderNo());

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()) //  Geeft statuscode 201 (created) i.p.v. verwachte 409 (conflict)
                .andExpect(content().string("An invoice already exists for order number: " + orderNo))
                .andReturn();

        System.out.println("Response Status: " + result.getResponse().getStatus()); // wordt niet geprint, blijkbaar wordt dit stuk niet bereikt omdat het hierboven al niet volgens verwachting gaat
        System.out.println("Response Content: " + result.getResponse().getContentAsString()); // idem als hierboven

        verify(orderRepository, times(1)).findById(orderNo);
        verify(orderService, times(1)).hasExistingInvoice(orderNo);
        verify(invoiceService, never()).createInvoice(any(Invoice.class), eq(orderNo));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateInvoice_thenBadRequest() throws Exception {

        // Arrange
        InvoiceRequest invalidInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest()
                .invoiceDate(null)
                .amount(-10.00)
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
        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();
        Invoice expectedInvoice = InvoiceFactory.randomInvoice().build();

        when(invoiceService.editInvoice(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(Invoice.class), ArgumentMatchers.eq(expectedInvoiceRequest.orderNo())))
                .thenReturn(expectedInvoice);

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
        mockMvc.perform(put("/api/invoice/{id}", expectedInvoice.getInvoiceNo())
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
        InvoiceRequest expectedInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();

        when(invoiceService.editInvoice(any(UUID.class), any(Invoice.class), eq(expectedInvoiceRequest.orderNo())))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found with this id."));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/invoice/{id}", invoiceNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedInvoiceRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditInvoice_thenBadRequest() throws Exception {

        // Arrange
        UUID invoiceNo = UUID.randomUUID();

        InvoiceRequest invalidInvoiceRequest = InvoiceRequestFactory.randomInvoiceRequest()
                .invoiceDate(null)
                .amount(-10.00)
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
