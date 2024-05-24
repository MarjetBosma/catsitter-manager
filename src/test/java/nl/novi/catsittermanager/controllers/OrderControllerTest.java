package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatRequestFactory;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.services.OrderService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class,  TestConfig.class})
@ActiveProfiles("test")

public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenAValidRequest_whenGetAllOrders_thenAllOrdersShouldBeReturned() throws Exception {

        Order expectedOrder = OrderFactory.randomOrder().build();
        List<Order> expectedOrderList = List.of(expectedOrder);

        when(orderService.getAllOrders()).thenReturn(expectedOrderList);

        mockMvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedOrderList.size()))
                .andExpect(jsonPath("$.[0].orderNo").value(expectedOrder.getOrderNo().toString()))
                .andExpect(jsonPath("$.[0].startDate").value(expectedOrder.getStartDate().toString()))
                .andExpect(jsonPath("$.[0].endDate").value(expectedOrder.getEndDate().toString()))
                .andExpect(jsonPath("$.[0].dailyNumberOfVisits").value(expectedOrder.getDailyNumberOfVisits()))
                .andExpect(jsonPath("$.[0].totalNumberOfVisits").value(expectedOrder.getTotalNumberOfVisits()))
                .andExpect(jsonPath("$.[0].tasks").value(expectedOrder.getTasks()));
//                .andExpect(jsonPath("$.[0].customer").value(expectedOrder.getCustomer().getUsername()))
//                .andExpect(jsonPath("$.[0].catsitter").value(expectedOrder.getCatsitter().getUsername()))
//                .andExpect(jsonPath("$.[0].invoice").value(expectedOrder.getInvoice().getInvoiceNo()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenNoOrdersAvailable_whenGetAllOrders_thenEmptyListShouldBeReturned() throws Exception {

        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenAValidRequest_whenGetOrder_thenOrderShouldBeReturned() throws Exception {

        Order expectedOrder = OrderFactory.randomOrder().build();

        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);

        mockMvc.perform(get("/api/order/" + expectedOrder.getOrderNo().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value(expectedOrder.getOrderNo().toString()))
                .andExpect(jsonPath("$.startDate").value(expectedOrder.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(expectedOrder.getEndDate().toString()))
                .andExpect(jsonPath("$.dailyNumberOfVisits").value(expectedOrder.getDailyNumberOfVisits()))
                .andExpect(jsonPath("$.totalNumberOfVisits").value(expectedOrder.getTotalNumberOfVisits()))
                .andExpect(jsonPath("$.tasks").value(expectedOrder.getTasks()));
//                .andExpect(jsonPath("$.[0].customer").value(expectedOrder.getCustomer().getUsername()))
//                .andExpect(jsonPath("$.[0].catsitter").value(expectedOrder.getCatsitter().getUsername()))
//                .andExpect(jsonPath("$.[0].invoice").value(expectedOrder.getInvoice().getInvoiceNo()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenInvalidOrderNo_whenGetOrder_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        UUID invalidOrderNo = UUID.randomUUID();
        final String errorMessage = "No order found with this id.";

        when(orderService.getOrder(invalidOrderNo)).thenThrow(new RecordNotFoundException(errorMessage));

        mockMvc.perform(get("/api/order/" + invalidOrderNo)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

}
