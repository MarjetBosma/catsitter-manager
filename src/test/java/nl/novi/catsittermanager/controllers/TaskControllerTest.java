package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.models.TaskFactory;
import nl.novi.catsittermanager.services.TaskService;
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

@WebMvcTest(TaskController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class,  TestConfig.class})
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetAllInvoices_thenAllInvoicesShouldBeReturned() throws Exception {
        // Arrange
        Task expectedTask = TaskFactory.randomTask().build();
        List<Task> expectedTaskList = List.of(expectedTask);

        when(taskService.getAllTasks()).thenReturn(expectedTaskList);

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
        mockMvc.perform(get("/api/tasks")
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
    void givenNoTasksAvailable_whenGetAllTasks_thenEmptyListShouldBeReturned() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

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
    void givenAValidRequest_whenGetTask_thenTaskShouldBeReturned() throws Exception {
        // Arrange
        Task expectedTask = TaskFactory.randomTask().build();

        when(taskService.getTask(expectedTask.getTaskNo())).thenReturn(expectedTask);

        TaskResponse expectedResponse = new TaskResponse(
                expectedTask.getTaskNo(),
                expectedTask.getTaskType(),
                expectedTask.getTaskInstruction(),
                expectedTask.getExtraInstructions(),
                expectedTask.getPriceOfTask(),
                expectedTask.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedTask);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/task/" + expectedTask.getTaskNo().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskNo").value(expectedResponse.taskNo().toString()))
                .andExpect(jsonPath("$.taskType").value(expectedResponse.taskType().toString()))
                .andExpect(jsonPath("$.taskInstruction").value(expectedResponse.taskInstruction()))
                .andExpect(jsonPath("$.extraInstructions").value(expectedResponse.extraInstructions()))
                .andExpect(jsonPath("$.priceOfTask").value(expectedResponse.priceOfTask()))
                .andExpect(jsonPath("$.orderNo").value(expectedResponse.orderNo().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidTaskNo_whenGetTask_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {
        // Arrange
        UUID invalidTaskNo = UUID.randomUUID();
        final String errorMessage = "No task found with this id.";

        when(taskService.getTask(invalidTaskNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/task/" + invalidTaskNo)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenCreateTask_thenTaskShouldBeReturned() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateTask_thenBadRequest() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditTask_thenEditedTaskShouldBeReturned() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditTask_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditInvoice_thenBadRequest() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteTask_thenTaskShouldBeDeleted() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteTask_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

    }
}


