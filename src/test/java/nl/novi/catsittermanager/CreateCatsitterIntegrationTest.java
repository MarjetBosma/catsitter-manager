package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.controllers.ExceptionController;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
@Import({ExceptionController.class, TestConfig.class})
public class CreateCatsitterIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CatsitterRepository catsitterRepository;

    Catsitter expectedCatsitter;

    CatsitterRequest request;

    @BeforeEach
    void setUp() {
        request = new CatsitterRequest("marietjemuk", "abc123", "Marietje Muk", "Straatweg 123, Nergenshuizen", "marietjemuk@gmail.com", "blablabla", null);
        User user = new User(request.username(), "encryptedPassword", Role.CATSITTER, true, request.name(), request.address(), request.email());
        expectedCatsitter = Catsitter.CatsitterBuilder()
                .username("marietjemuk")
                .name("Marietje Muk")
                .address("Straatweg 123, Nergenshuizen")
                .email("marietjemuk@gmail.com")
                .about("blablabla")
                .orders(new ArrayList<>())
                .image(null)
                .build();

        when(catsitterRepository.save(any(Catsitter.class))).thenReturn(expectedCatsitter);
    }

    @AfterEach
    void tearDown() {
        expectedCatsitter = null;
        request = null;
    }

    @Test
    void createCatsitter() throws Exception {

        // Arrange
        String jsonInput = objectMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/catsitter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(request.username()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(request.address()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(request.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.about").value(request.about()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].orders").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").doesNotExist());

        // Assert
        verify(catsitterRepository, times(1)).save(any(Catsitter.class));
    }

    @Test
    void createCatsitter_WithInvalidInput_ShouldReturnBadRequest() throws Exception {

        // Arrange
        CatsitterRequest invalidRequest = new CatsitterRequest(null, null, null, null, null, null, null);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/catsitter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Full name is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required"));

        // Assert
        verifyNoInteractions(catsitterRepository);
    }
}

