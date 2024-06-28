package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.CustomerRequestFactory;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.services.CustomerService;
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

@WebMvcTest(CustomerController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

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
    void givenAValidRequest_whenGetAllCustomers_thenAllCustomersShouldBeReturned() throws Exception {

        // Arrange
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();
        List<Customer> expectedCustomerList = List.of(expectedCustomer);

        when(customerService.getAllCustomers()).thenReturn(expectedCustomerList);

        CustomerResponse expectedResponse = new CustomerResponse(
                expectedCustomer.getUsername(),
                expectedCustomer.getName(),
                expectedCustomer.getAddress(),
                expectedCustomer.getEmail(),
                expectedCustomer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                expectedCustomer.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList()
        );

        String content = objectMapper.writeValueAsString(expectedCustomerList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedCustomerList.size()))
                .andExpect(jsonPath("$.[0].username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.[0].name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.[0].address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.[0].email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoCustomersAvailable_whenGetAllCustomers_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetCustomer_thenCustomerShouldBeReturned() throws Exception {

        // Arrange
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerService.getCustomer(expectedCustomer.getUsername())).thenReturn(expectedCustomer);

        CustomerResponse expectedResponse = new CustomerResponse(
                expectedCustomer.getUsername(),
                expectedCustomer.getName(),
                expectedCustomer.getAddress(),
                expectedCustomer.getEmail(),
                expectedCustomer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                expectedCustomer.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList()
        );

        String content = objectMapper.writeValueAsString(expectedCustomer);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/customer/" + expectedCustomer.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidUsername_whenGetCustomer_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String invalidUsername = "testcustomer";
        final String errorMessage = "No customer found with this username.";

        when(customerService.getCustomer(invalidUsername)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/customer/" + invalidUsername)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllCatsByCustomer_thenAllCatsShouldBeReturned() throws Exception {

        // Arrange
        String username = "testcustomer";
        Cat expectedCat = CatFactory.randomCat().build();
        List<Cat> expectedCatList = List.of(expectedCat);

        when(customerService.getAllCatsByCustomer(username)).thenReturn(expectedCatList);

        CatResponse expectedResponse = new CatResponse(
                expectedCat.getId(),
                expectedCat.getName(),
                expectedCat.getDateOfBirth().toString(),
                expectedCat.getGender(),
                expectedCat.getBreed(),
                expectedCat.getGeneralInfo(),
                expectedCat.getSpayedOrNeutered(),
                expectedCat.getVaccinated(),
                expectedCat.getVeterinarianName(),
                expectedCat.getPhoneVet(),
                expectedCat.getMedicationName(),
                expectedCat.getMedicationDose(),
                expectedCat.getOwner().getUsername(),
                expectedCat.getImage()
        );

        String content = objectMapper.writeValueAsString(expectedCatList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/customer/" + username + "/cats")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedCatList.size()))
                .andExpect(jsonPath("$.[0].id").value(expectedCat.getId().toString()))
                .andExpect(jsonPath("$.[0].name").value(expectedCat.getName()))
                .andExpect(jsonPath("$.[0].dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.[0].gender").value(expectedCat.getGender()))
                .andExpect(jsonPath("$.[0].generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(jsonPath("$.[0].spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(jsonPath("$.[0].vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(jsonPath("$.[0].veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(jsonPath("$.[0].phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(jsonPath("$.[0].medicationName").value(expectedCat.getMedicationName()))
                .andExpect(jsonPath("$.[0].medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(jsonPath("$.[0].ownerUsername").value(expectedCat.getOwner().getUsername()))
                .andExpect(jsonPath("$.[0].image").value(expectedCat.getImage()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoCatsAvailableForASpecificCustomer_whenGetAllCatsByCustomer_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        String username = "testcustomer";

        when(customerService.getAllCatsByCustomer(username)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/customer/" + username + "/cats")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllOrdersByCustomer_thenAllOrdersShouldBeReturned() throws Exception {

        // Arrange
        String customerUsername = "customerUsername";
        Customer customer = new Customer();
        customer.setUsername(customerUsername);

        Order expectedOrder = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .totalNumberOfVisits(10)
                .tasks(new ArrayList<>())
                .customer(customer)
                .catsitter(new Catsitter())
                .build();

        List<Task> tasks = List.of(Task.builder().build());
        for (Task task : tasks) {
            task.setOrder(expectedOrder);
        }

        List<Order> expectedOrderList = List.of(expectedOrder);

        when(customerService.getAllOrdersByCustomer(customerUsername)).thenReturn(expectedOrderList);

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
        mockMvc.perform(get("/api/customer/" + customerUsername + "/orders")
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
    void givenNoOrdersAvailableForASpecificCustomer_whenGetAllOrdersByCustomer_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        String username = "testcustomer";

        when(customerService.getAllOrdersByCustomer(username)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/customer/" + username + "/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenCreateCustomer_thenCustomerShouldBeReturned() throws Exception {

        // Arrange
        CustomerRequest expectedCustomerRequest = CustomerRequestFactory.randomCustomerRequest().build();
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerService.createCustomer(any(Customer.class)))
                .thenReturn(expectedCustomer);

        CustomerResponse expectedResponse = new CustomerResponse(
                expectedCustomer.getUsername(),
                expectedCustomer.getName(),
                expectedCustomer.getAddress(),
                expectedCustomer.getEmail(),
                expectedCustomer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                expectedCustomer.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList()
        );

        String content = objectMapper.writeValueAsString(expectedCustomerRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateCustomer_thenBadRequest() throws Exception {

        // Arrange
        CustomerRequest invalidCustomerRequest = CustomerRequestFactory.randomCustomerRequest()
                .username(null)
                .name(null)
                .address(null)
                .email(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditCustomer_thenEditedCustomerShouldBeReturned() throws Exception {

        // Arrange
        CustomerRequest expectedCustomerRequest = CustomerRequestFactory.randomCustomerRequest().build();
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerService.editCustomer(any(String.class), any(Customer.class)))
                .thenReturn(expectedCustomer);

        CustomerResponse expectedResponse = new CustomerResponse(
                expectedCustomer.getUsername(),
                expectedCustomer.getName(),
                expectedCustomer.getAddress(),
                expectedCustomer.getEmail(),
                expectedCustomer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                expectedCustomer.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList()
        );

        String content = objectMapper.writeValueAsString(expectedCustomerRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/customer/{id}", expectedCustomer.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username().toString()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditCustomer_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testcustomer";
        CustomerRequest expectedCustomerRequest = CustomerRequestFactory.randomCustomerRequest().build();

        when(customerService.editCustomer(eq("testcustomer"), any(Customer.class)))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username."));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customer/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCustomerRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditCustomer_thenBadRequest() throws Exception {

        // Arrange
        String username = "testcustomer";

        CustomerRequest invalidCustomerRequest = CustomerRequestFactory.randomCustomerRequest()
                .username(null)
                .name(null)
                .address(null)
                .email(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteCustomer_thenCustomerShouldBeDeleted() throws Exception {

        // Arrange
        String username = "testcustomer";

        when(customerService.deleteCustomer(username)).thenReturn(username);

        // Act & Assert
        mockMvc.perform(delete("/api/customer/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer with username " + username + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteCustomer_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testcustomer";
        final String errorMessage = "No customer found with this id";

        when(customerService.deleteCustomer(username)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/customer/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}
