package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CreateCustomerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

//    @Autowired
//    CustomerService customerService;

    @MockBean
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
                .cats(null) // "Cannot resolve method cats in Userbuilder". Nee, die komt net als orders uit de CustomerBuilder...
                .orders(null)
                .build();
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void createCustomer() throws Exception {
        // Given
        String jsonInput = objectMapper.writeValueAsString(request);

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);
        when(customerRepository.save(any(Customer.class))).thenThrow(new DataIntegrityViolationException("Invalid customer data"));

        // When
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

//        String jsonResponse = result.getResponse().getContentAsString();
//
//        CustomerResponse response = objectMapper.readValue(jsonResponse, CustomerResponse.class);
//
//        assertNotNull(response);
//        assertEquals(request.username(), response.username());
//        assertEquals(request.username(), response.name());
//        assertEquals(request.address(), response.address());
//        assertEquals(request.email(), response.email());
//        assertNull(response.cats());
//        assertNull(response.orders());

        verify(customerRepository, times(1)).save(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);
    }
}
