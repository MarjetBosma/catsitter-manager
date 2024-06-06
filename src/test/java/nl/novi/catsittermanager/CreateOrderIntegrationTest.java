package nl.novi.catsittermanager;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.services.CatsitterService;
import nl.novi.catsittermanager.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
class CreateOrderIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderRepository orderRepository;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatsitterService catsitterService;

    private String jsonInput;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        jsonInput = """
                {
                    "startDate": "2024-06-01",
                    "endDate": "2024-06-10",
                    "dailyNumberOfVisits": 2,
                    "totalNumberOfVisits": 18,
                    "customerUsername": "pietjepuk",
                    "catsitterUsername": "marietjemuk"
                }
                 \s""";

        when(customerService.getCustomer(anyString()))
                .thenAnswer(invocation -> {
                    String username = invocation.getArgument(0);
                    Customer customer = new Customer();
                    customer.setUsername(username);
                    return customer;
                });

        when(catsitterService.getCatsitter(anyString()))
                .thenAnswer(invocation -> {
                    String username = invocation.getArgument(0);
                    Catsitter catsitter = new Catsitter();
                    catsitter.setUsername(username);
                    return catsitter;
                });

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    String customerUsername = objectMapper.readTree(jsonInput).get("customerUsername").asText();
                    Customer customer = customerService.getCustomer(customerUsername);
                    order.setCustomer(customer);
                    String catsitterUsername = objectMapper.readTree(jsonInput).get("catsitterUsername").asText();
                    Catsitter catsitter = catsitterService.getCatsitter(catsitterUsername);
                    order.setCatsitter(catsitter);
                    order.setOrderNo(uuid);
                    return order;
                });
    }

    @AfterEach
    void tearDown() {
        jsonInput = null;
    }

    @Test
    void createOrder() throws Exception {

        // Arrange
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println("Response Headers: " + response.getHeaderNames());

        Assertions.assertNotNull(result.getResponse().getHeader("location"));
        Assertions.assertTrue(result.getResponse().getHeader("location").endsWith("/order/" + uuid));

        verify(orderRepository, times(1)).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();

        Assertions.assertEquals(LocalDate.of(2024, 6, 1), capturedOrder.getStartDate());
        Assertions.assertEquals(LocalDate.of(2024, 6, 10), capturedOrder.getEndDate());
        Assertions.assertEquals(2, capturedOrder.getDailyNumberOfVisits());
        Assertions.assertEquals(18, capturedOrder.getTotalNumberOfVisits());
        Assertions.assertEquals("pietjepuk", capturedOrder.getCustomer().getUsername());
        Assertions.assertEquals("marietjemuk", capturedOrder.getCatsitter().getUsername());
    }

    @Test
    void createOrder_WithInvalidInput_ShouldReturnBadRequest() throws Exception {

        // Arrange
        String invalidJsonInput = "{}";

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonInput))
                .andExpect(status().isBadRequest());
    }
}


