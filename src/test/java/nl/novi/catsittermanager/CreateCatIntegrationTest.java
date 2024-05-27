package nl.novi.catsittermanager;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
class CreateCatIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CatRepository catRepository;

    @MockBean
    CustomerService customerService;

    private String jsonInput;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        jsonInput = """
                {
                "name": "Firsa",
                "dateOfBirth": "2006-07-01",
                "gender": "V",
                "breed": "Europese Korthaar",
                "generalInfo": "Vriendelijke, maar verlegen kat. Verstopt zich vaak voor vreemden.",
                "spayedOrNeutered": true,
                "vaccinated": true,
                "veterinarianName": "Dierenkliniek Zuilen",
                "phoneVet": "030-1234567",
                "medicationName": "Semintra",
                "medicationDose": "2ml 1dd",
                "ownerUsername": "marjetbosma"
                 }
                 \s""";

        when(customerService.getCustomer(anyString()))
                .thenAnswer(invocation -> {
                    String username = invocation.getArgument(0);
                    Customer customer = new Customer();
                    customer.setUsername(username);
                    return customer;
                });


        when(catRepository.save(any(Cat.class)))
                .thenAnswer(invocation -> {
                    Cat cat = invocation.getArgument(0);
                    String ownerUsername = objectMapper.readTree(jsonInput).get("ownerUsername").asText();
                    Customer owner = customerService.getCustomer(ownerUsername);
                    cat.setOwner(owner);
                    cat.setId(uuid);
                    return cat;
                });
    }

    @AfterEach
    void tearDown() {
        jsonInput = null;
    }

    @Test
    void createCat() throws Exception {
        // Given
        ArgumentCaptor<Cat> catArgumentCaptor = ArgumentCaptor.forClass(Cat.class);

        // When & Then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertNotNull(result.getResponse().getHeader("location"));
        Assertions.assertTrue(result.getResponse().getHeader("location").endsWith("/cat/" + uuid));

        verify(catRepository, times(1)).save(catArgumentCaptor.capture());
        Cat capturedCat = catArgumentCaptor.getValue();

        assertEquals("Firsa", capturedCat.getName());
        assertEquals(LocalDate.of(2006, 7, 1), capturedCat.getDateOfBirth());
        assertEquals("V", capturedCat.getGender());
        assertEquals("Europese Korthaar", capturedCat.getBreed());
        assertEquals("Vriendelijke, maar verlegen kat. Verstopt zich vaak voor vreemden.", capturedCat.getGeneralInfo());
        assertEquals(true, capturedCat.getVaccinated());
        assertEquals(true, capturedCat.getSpayedOrNeutered());
        assertEquals("Dierenkliniek Zuilen", capturedCat.getVeterinarianName());
        assertEquals("030-1234567", capturedCat.getPhoneVet());
        assertEquals("Semintra", capturedCat.getMedicationName());
        assertEquals("2ml 1dd", capturedCat.getMedicationDose());

        String owner = JsonPath.read(
                result.getResponse().getContentAsString(),
                "$.ownerUsername");

        assertEquals("marjetbosma", owner);

    }

    @Test
    void createCat_WithInvalidInput_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidJsonInput = "{}";

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonInput))
        // Then
        .andExpect(status().isBadRequest());
    }
}
