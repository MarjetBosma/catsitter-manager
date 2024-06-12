package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.TaskRequestFactory;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.models.TaskFactory;
import nl.novi.catsittermanager.services.TaskService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

@WebMvcTest(TaskController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

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

        // Arrange
        TaskRequest expectedTaskRequest = TaskRequestFactory.randomTaskRequest().build();
        Task expectedTask = TaskFactory.randomTask().build();

        when(taskService.createTask(any(Task.class), eq(expectedTaskRequest.orderNo())))
                .thenReturn(expectedTask);

        String content = objectMapper.writeValueAsString(expectedTaskRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskNo").value(expectedTask.getTaskNo().toString()))
                .andExpect(jsonPath("$.taskType").value(expectedTask.getTaskType().toString()))
                .andExpect(jsonPath("$.taskInstruction").value(expectedTask.getTaskInstruction()))
                .andExpect(jsonPath("$.extraInstructions").value(expectedTask.getExtraInstructions()))
                .andExpect(jsonPath("$.priceOfTask").value(expectedTask.getPriceOfTask()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateTask_thenBadRequest() throws Exception {

        // Arrange
        TaskRequest invalidTaskRequest = TaskRequestFactory.randomTaskRequest()
                .taskType(null)
                .taskInstruction(null)
                .extraInstructions(null)
                .priceOfTask(-10.00)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTaskRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditTask_thenEditedTaskShouldBeReturned() throws Exception {

        // Arrange
        TaskRequest expectedTaskRequest = TaskRequestFactory.randomTaskRequest().build();
        Task expectedTask = TaskFactory.randomTask().build();

        when(taskService.editTask(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(Task.class), ArgumentMatchers.eq(expectedTaskRequest.orderNo())))
                .thenReturn(expectedTask);
        TaskResponse expectedTaskResponse = new TaskResponse(
                expectedTask.getTaskNo(),
                expectedTask.getTaskType(),
                expectedTask.getTaskInstruction(),
                expectedTask.getExtraInstructions(),
                expectedTask.getPriceOfTask(),
                expectedTask.getOrder().getOrderNo()
        );

        String content = objectMapper.writeValueAsString(expectedTaskRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/task/{taskNo}", expectedTask.getTaskNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskNo").value(expectedTask.getTaskNo().toString()))
                .andExpect(jsonPath("$.taskType").value(expectedTask.getTaskType().toString()))
                .andExpect(jsonPath("$.taskInstruction").value(expectedTask.getTaskInstruction()))
                .andExpect(jsonPath("$.extraInstructions").value(expectedTask.getExtraInstructions()))
                .andExpect(jsonPath("$.priceOfTask").value(expectedTask.getPriceOfTask()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditTask_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID taskNo = UUID.randomUUID();
        TaskRequest expectedTaskRequest = TaskRequestFactory.randomTaskRequest().build();

        when(taskService.editTask(any(UUID.class), any(Task.class), eq(expectedTaskRequest.orderNo())))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No task found with this id."));

        //  Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/task/{id}", taskNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedTaskRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenEditTask_thenBadRequest() throws Exception {

        //  Arrange
        UUID taskNo = UUID.randomUUID();

        TaskRequest invalidTaskRequest = TaskRequestFactory.randomTaskRequest()
                .taskType(null)
                .taskInstruction(null)
                .extraInstructions(null)
                .priceOfTask(-10.00)
                .orderNo(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/task/{id}", taskNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTaskRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteTask_thenTaskShouldBeDeleted() throws Exception {

        // Arrange
        UUID taskNo = UUID.randomUUID();

        when(taskService.deleteTask(taskNo)).thenReturn(taskNo);

        // Act & Assert
        mockMvc.perform(delete("/api/task/{id}", taskNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with id " + taskNo + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteTask_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        UUID invalidTaskNo = UUID.randomUUID();
        final String errorMessage = "No task found with this id";

        when(taskService.deleteTask(invalidTaskNo)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/task/{id}", invalidTaskNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}


