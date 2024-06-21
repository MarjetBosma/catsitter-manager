package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.config.SecurityConfig;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.UserRequestFactory;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.models.UserFactory;
import nl.novi.catsittermanager.services.UserService;
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

@WebMvcTest(CustomerController.class)
@Import({JwtUtil.class, JwtAuthorizationFilter.class, SecurityConfig.class, TestConfig.class})
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

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
    void givenAValidRequest_WhenGetAllUsers_thenAllUsersShouldBeReturned() throws Exception {

        // Arrange
        User expectedUser = UserFactory.randomUser().build();

        List<User> expectedUserList = List.of(expectedUser);

        when(userService.getAllUsers()).thenReturn(expectedUserList);

        UserResponse expectedResponse = new UserResponse(
                expectedUser.getUsername(),
                expectedUser.getRole(),
                expectedUser.getEnabled(),
                expectedUser.getName(),
                expectedUser.getAddress(),
                expectedUser.getEmail()
        );

        String content = objectMapper.writeValueAsString(expectedUserList);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedUserList.size()))
                .andExpect(jsonPath("$.[0].username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.[0].role").value(expectedResponse.role().toString()))
                .andExpect(jsonPath("$.[0].enabled").value(expectedResponse.enabled()))
                .andExpect(jsonPath("$.[0].name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.[0].address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.[0].email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenNoUsersAvailable_whenGetAllUsers_thenEmptyListShouldBeReturned() throws Exception {

        // Arrange
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenGetUser_thenUserShouldBeReturned() throws Exception {

        // Arrange
        User expectedUser = UserFactory.randomUser().build();

        when(userService.getUser(expectedUser.getUsername())).thenReturn(expectedUser);

        UserResponse expectedResponse = new UserResponse(
                expectedUser.getUsername(),
                expectedUser.getRole(),
                expectedUser.getEnabled(),
                expectedUser.getName(),
                expectedUser.getAddress(),
                expectedUser.getEmail()
        );

        String content = objectMapper.writeValueAsString(expectedUser);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(get("/api/user/" + expectedUser.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.role").value(expectedResponse.role().toString()))
                .andExpect(jsonPath("$.enabled").value(expectedResponse.enabled()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidUsername_whenGetUser_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String invalidUsername = "testuser";
        final String errorMessage = "No user found with this username.";

        when(userService.getUser(invalidUsername)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/user/" + invalidUsername)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenCreateNewAdmin_thenAdminShouldBeReturned() throws Exception {

        // Arrange
        UserRequest expectedUserRequest = UserRequestFactory.randomUserRequest().build();
        User expectedUser = UserFactory.randomUser().build();

        when(userService.createAdminAccount(any(User.class)))
                .thenReturn(expectedUser);

        UserResponse expectedResponse = new UserResponse(
                expectedUser.getUsername(),
                expectedUser.getRole(),
                expectedUser.getEnabled(),
                expectedUser.getName(),
                expectedUser.getAddress(),
                expectedUser.getEmail()
        );

        String content = objectMapper.writeValueAsString(expectedUserRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.role").value(expectedResponse.role().toString()))
                .andExpect(jsonPath("$.enabled").value(expectedResponse.enabled()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidData_whenCreateNewAdmin_thenBadRequest() throws Exception {

        // Arrange
        UserRequest invalidUserRequest = UserRequestFactory.randomUserRequest()
                .username(null)
                .role(null)
                .enabled(null)
                .name(null)
                .address(null)
                .email(null)
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAValidRequest_whenEditUser_thenEditedUserShouldBeReturned() throws Exception {

        // Arrange
        UserRequest expectedUserRequest = UserRequestFactory.randomUserRequest().build();
        User expectedUser = UserFactory.randomUser().build();

        when(userService.editUser(any(String.class), any(User.class)))
                .thenReturn(expectedUser);

        UserResponse expectedResponse = new UserResponse(
                expectedUser.getUsername(),
                expectedUser.getRole(),
                expectedUser.getEnabled(),
                expectedUser.getName(),
                expectedUser.getAddress(),
                expectedUser.getEmail()
        );

        String content = objectMapper.writeValueAsString(expectedUserRequest);
        System.out.println(content);

        // Act & Assert
        mockMvc.perform(put("/api/user/{id}", expectedUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedResponse.username()))
                .andExpect(jsonPath("$.role").value(expectedResponse.role().toString()))
                .andExpect(jsonPath("$.enabled").value(expectedResponse.enabled()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.address").value(expectedResponse.address()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenEditUser_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testuser";
        UserRequest expectedUserRequest = UserRequestFactory.randomUserRequest().build();

        when(userService.editUser(eq("testuser"), any(User.class)))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username."));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedUserRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenDeleteUser_thenUserShouldBeDeleted() throws Exception {

        // Arrange
        String username = "testuser";

        when(userService.deleteUser(username)).thenReturn(username);

        // Act & Assert
        mockMvc.perform(delete("/api/user/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User with username " + username + " removed from database."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleteUser_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        // Arrange
        String username = "testuser";
        final String errorMessage = "No user found with this id";

        when(userService.deleteUser(username)).thenThrow(new RecordNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(delete("/api/user/{id}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }
}

