package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.dtos.login.LoginRequest;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    private String loginRequestJson;

    @BeforeEach
    void setUp() {
        loginRequestJson = """
                {
                    "username": "testuser",
                    "password": "testpassword"
                }
                """;
    }

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String token = "mock-token";
        LoginRequest loginRequest = new LoginRequest("testuser", "testpassword");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                new ArrayList<>()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, loginRequest.getPassword(), userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.createToken(userDetails.getUsername())).thenReturn(token);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(loginRequest.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));
    }

    @Test
    void login_ShouldReturnForbidden_WhenUnexpectedPrincipal() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("invalidPrincipal", "invalidPassword"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(containsString("Unexpected authentication principal: invalidPrincipal")));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid username or password"));
    }
}



