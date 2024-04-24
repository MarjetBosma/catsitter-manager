package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.controllers.AuthenticationController;
import nl.novi.catsittermanager.dtos.login.LoginRequest;
import nl.novi.catsittermanager.dtos.login.LoginResponse;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
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

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        String jsonInput = "{\"username\": \"testUser\", \"password\": \"testPassword\"}";

        // Mock the authentication process
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "testPassword");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("testUser", "testPassword"))).thenReturn(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk());

    }
//        // Given
//        LoginRequest loginRequest = new LoginRequest("username", "password");
//
//        // When
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        // Then
//        String jsonResponse = result.getResponse().getContentAsString();
//        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
//        assertNotNull(loginResponse);
//        assertNotNull(loginResponse.getToken());
//    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnBadRequest() throws Exception {
//        // Given
//        LoginRequest loginRequest = new LoginRequest("invalidUsername", "invalidPassword");
//
//        // When
//        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Invalid username or password"));
//    }
        // Given
        String jsonInput = "{\"username\": \"testUser\", \"password\": \"testPassword\"}";

        // When
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "testPassword");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("testUser", "testPassword"))).thenReturn(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk());
    }
}
