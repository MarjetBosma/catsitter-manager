package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.CatsitterRequestFactory;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.services.CatsitterService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatsitterController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class,  TestConfig.class})
@ActiveProfiles("test")
public class CatsitterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatsitterService catsitterService;

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
    void givenAValidRequest_whenGetAllCatsitters_thenAllCatsittersShouldBeReturned() throws Exception {

        // Arrange
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();
        List<Catsitter> expectedCatsitterList = List.of(expectedCatsitter);

        when(catsitterService.getAllCatsitters()).thenReturn(expectedCatsitterList);

        CatsitterResponse expectedResponse = new CatsitterResponse(
                expectedCatsitter.getUsername(),
                expectedCatsitter.getName(),
                expectedCatsitter.getAddress(),
                expectedCatsitter.getEmail(),
                expectedCatsitter.getAbout(),
                expectedCatsitter.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList(),
                expectedCatsitter.getImage()
        );

        String content = objectMapper.writeValueAsString(expectedCatsitterList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/catsitters")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedCatsitterList.size()))
                .andExpect(jsonPath("$.[0].username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.[0].name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.[0].address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.[0].email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.[0].about").value(expectedResponse.about()))
                .andExpect(jsonPath("$.[0].image").value(expectedResponse.image()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenNoCatsittersAvailable_whenGetAllCatsitters_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        when(catsitterService.getAllCatsitters()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/catsitters")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenAValidRequest_whenGetCatsitter_thenCatsitterShouldBeReturned() throws Exception {

        // Arrange
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterService.getCatsitter(expectedCatsitter.getUsername())).thenReturn(expectedCatsitter);

        CatsitterResponse expectedResponse = new CatsitterResponse(
                expectedCatsitter.getUsername(),
                expectedCatsitter.getName(),
                expectedCatsitter.getAddress(),
                expectedCatsitter.getEmail(),
                expectedCatsitter.getAbout(),
                expectedCatsitter.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList(),
                expectedCatsitter.getImage()
        );

        String content = objectMapper.writeValueAsString(expectedCatsitter);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/catsitter/" + expectedCatsitter.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.about").value(expectedResponse.about()))
                .andExpect(jsonPath("$.image").value(expectedResponse.image()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenInvalidUsername_whenGetCatsitter_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String invalidUsername = "testcatsitter";
        final String errorMessage = "No catsitter found with this id.";

        when(catsitterService.getCatsitter(invalidUsername)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/catsitter/" + invalidUsername)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenAValidRequest_whenGetAllOrdersByCatsitter_thenAllOrdersShouldBeReturned() throws Exception {

        // Arrange
        String username = "testcatsitter";
        Order expectedOrder = OrderFactory.randomOrder().build();
        List<Order> expectedOrderList = List.of(expectedOrder);

        when(catsitterService.getAllOrdersByCatsitter(username)).thenReturn(expectedOrderList);

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
        mockMvc.perform(get("/api/catsitter/" + username + "/orders")
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
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenNoOrdersAvailableForASpecificCatsitter_whenGetAllOrdersByCatsitter_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        String username = "testcatsitter";

        when(catsitterService.getAllOrdersByCatsitter(username)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/catsitter/" + username + "/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void givenAValidRequest_whenCreateCatsitter_thenCatsitterShouldBeReturned() throws Exception {

        // Arrange
        CatsitterRequest expectedCatsitterRequest = CatsitterRequestFactory.randomCatsitterRequest().build();
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterService.createCatsitter(any(Catsitter.class)))
                .thenReturn(expectedCatsitter);

        CatsitterResponse expectedResponse = new CatsitterResponse(
                expectedCatsitter.getUsername(),
                expectedCatsitter.getName(),
                expectedCatsitter.getAddress(),
                expectedCatsitter.getEmail(),
                expectedCatsitter.getAbout(),
                expectedCatsitter.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList(),
                expectedCatsitter.getImage()
        );

        String content = objectMapper.writeValueAsString(expectedCatsitterRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(post("/api/catsitter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.about").value(expectedResponse.about()))
                .andExpect(jsonPath("$.image").value(expectedResponse.image()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateCatsitter_thenBadRequest() throws Exception {

        // Arrange
        CatsitterRequest invalidCatsitterRequest = CatsitterRequestFactory.randomCatsitterRequest()
                .username(null)
                .name(null)
                .address(null)
                .email(null)
                .about(null)
                .image(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/catsitter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCatsitterRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(catsitterService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditCatsitter_thenEditedCatsitterShouldBeReturned() throws Exception {

        // Arrange
        CatsitterRequest expectedCatsitterRequest = CatsitterRequestFactory.randomCatsitterRequest().build();
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterService.editCatsitter(any(String.class), any(Catsitter.class)))
                .thenReturn(expectedCatsitter);

        CatsitterResponse expectedResponse = new CatsitterResponse(
                expectedCatsitter.getUsername(),
                expectedCatsitter.getName(),
                expectedCatsitter.getAddress(),
                expectedCatsitter.getEmail(),
                expectedCatsitter.getAbout(),
                expectedCatsitter.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList(),
                expectedCatsitter.getImage()
        );

        String content = objectMapper.writeValueAsString(expectedCatsitterRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/catsitter/{id}", expectedCatsitter.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.about").value(expectedResponse.about()))
                .andExpect(jsonPath("$.image").value(expectedResponse.image()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditCatsitter_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testcatsitter";
        CatsitterRequest expectedCatsitterRequest = CatsitterRequestFactory.randomCatsitterRequest().build();

        when(catsitterService.editCatsitter(eq("testcatsitter"), any(Catsitter.class)))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username."));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/catsitter/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCatsitterRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditCatsitter_thenBadRequest() throws Exception {

        // Arrange
        String username = "testCatsitter";

        CatsitterRequest invalidCatsitterRequest = CatsitterRequestFactory.randomCatsitterRequest()
                .username(null)
                .name(null)
                .address(null)
                .email(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/catsitter/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCatsitterRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(catsitterService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteCatsitter_thenOrderShouldBeDeleted() throws Exception {

        // Arrange
        String username = "testcatsitter";

        when(catsitterService.deleteCatsitter(username)).thenReturn(username);

        // Act & Assert
        mockMvc.perform(delete("/api/catsitter/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Catsitter with username " + username + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteCatsitter_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testcatsitter";
        final String errorMessage = "No catsitter found with this id";

        when(catsitterService.deleteCatsitter(username)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/catsitter/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}
