package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.dtos.login.LoginRequest;
import nl.novi.catsittermanager.dtos.login.LoginResponse;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.services.UserService;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

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
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("username", "password"));
    }

    // todo: Geeft melding "content type not set", ondanks diverse aanpassingen waaronder in de AuthenticationController
    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {

        // Arrange
        LoginRequest loginRequest = new LoginRequest("username", "password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String token = "mocked_token";
        when(jwtUtil.createToken(anyString())).thenReturn(token);

        // Act
        String jsonInput = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(token))
                .andReturn();

        // Assert
        String jsonResponse = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        Assertions.assertNotNull(loginResponse);
        Assertions.assertEquals(token, loginResponse.getToken());
    }

    // todo: Geeft status 200 terug i.p.v. 401
    @Test
    void login_WithInvalidCredentials_ShouldReturnBadRequest() throws Exception {

        // Arrange
        LoginRequest loginRequest = new LoginRequest("", "");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act
        String jsonInput = objectMapper.writeValueAsString(loginRequest);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}
