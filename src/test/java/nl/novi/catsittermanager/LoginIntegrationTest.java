package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.controllers.AuthenticationController;
import nl.novi.catsittermanager.dtos.login.LoginRequest;
import nl.novi.catsittermanager.dtos.login.LoginResponse;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.repositories.UserRepository;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @MockBean
    private UserRepository userRepository;

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("username", "password");

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnBadRequest() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("invalidUsername", "invalidPassword");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}
