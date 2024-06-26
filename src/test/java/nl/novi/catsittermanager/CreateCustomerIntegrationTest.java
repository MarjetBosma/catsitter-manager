package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.controllers.ExceptionController;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
@Import({ExceptionController.class, TestConfig.class})
class CreateCustomerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    Customer expectedCustomer;

    CustomerRequest request;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest("pietjepuk", "qwerty", "Pietje Puk", "Straatweg 231, Ergenshuizen", "pietjepuk@gmail.com");
        User user = new User(request.username(), "encryptedPassword", Role.CUSTOMER, true, request.name(), request.address(), request.email());
        expectedCustomer = Customer.CustomerBuilder()
                .username("pietjepuk")
                .name("Pietje Puk")
                .address("Straatweg 231, Ergenshuizen")
                .email("pietjepuk@gmail.com")
                .cats((new ArrayList<>()))
                .orders((new ArrayList<>()))
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);
    }

    @AfterEach
    void tearDown() {
        expectedCustomer = null;
        request = null;
    }

    @Test
    void createCustomer() throws Exception {

        // Arrange
        String jsonInput = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(request.username()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(request.address()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(request.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].cats").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].orders").doesNotExist());

        //  Assert
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_WithInvalidInput_ShouldReturnBadRequest() throws Exception {

        // Arrange
        CustomerRequest invalidRequest = new CustomerRequest(null, null, null, null, null);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Full name is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required"));

        // Assert
        verifyNoInteractions(customerRepository);
    }
}
