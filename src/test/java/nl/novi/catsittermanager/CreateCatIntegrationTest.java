package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.services.CatService;
import nl.novi.catsittermanager.services.CustomerService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CreateCatIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CatService catService;

    @MockBean
    CatRepository catRepository;

    @MockBean
    CustomerService customerService;

    @MockBean
    Customer customer;

    private String jsonInput;

    Cat expectedCat;

    CatRequest request;

    @BeforeEach
    void setUp() {
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
                  """;

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
                    return cat;
                });

//        LocalDate dateOfBirth = LocalDate.of(2006, 7, 1);
//        request = new CatRequest("Firsa", dateOfBirth, "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat. Verstopt zich vaak voor vreemden.", true, true, "Dierenkliniek Zuilen", "030-1234567", "Semintra", "2ml 1dd", "marjetbosma", null);
//        expectedCat = Cat.builder()
//                .name("Firsa")
//                .dateOfBirth(dateOfBirth)
//                .gender("V")
//                .breed("Europese Korthaar")
//                .generalInfo("Vriendelijke, maar verlegen kat. Verstopt zich vaak voor vreemden.")
//                .spayedOrNeutered(true)
//                .vaccinated(true)
//                .veterinarianName("Dierenkliniek Zuilen")
//                .phoneVet("030-1234567")
//                .medicationName("Semintra")
//                .medicationDose("2ml 1dd")
//                .image(null)
//                .build();

//        when(catRepository.save(any(Cat.class))).thenReturn(expectedCat);

    }

    @AfterEach
    void tearDown() {
        jsonInput = null;
    }
//@AfterEach
//void tearDown() {
//    expectedCat = null;
//    request = null;
//}

    @Test
    void createCat() throws Exception {
        ArgumentCaptor<Cat> catArgumentCaptor = ArgumentCaptor.forClass(Cat.class);
        ArgumentCaptor<String> ownerUsernameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn();

        // Assert that the Location header matches the expected pattern
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String createdId = jsonNode.get("id").asText();
        MatcherAssert.assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/cat/" + createdId));

        // Verify that catRepository.save() is called with the expected Cat object
        verify(catRepository, times(1)).save(catArgumentCaptor.capture());
        Cat capturedCat = catArgumentCaptor.getValue();

        // Verify that catService.createCat() is called with the captured Cat object and ownerUsername
        verify(catService, times(1)).createCat(capturedCat, "marjetbosma");

        // Optionally, assert specific properties of the captured Cat object if needed
        assertEquals("Firsa", capturedCat.getName());
        assertEquals(LocalDate.of(2006, 7, 1), capturedCat.getDateOfBirth());
        // Add more assertions as needed

        // Assert that the ownerUsername passed to catService.createCat() matches the expected value
        verify(catService, times(1)).createCat(any(), ownerUsernameArgumentCaptor.capture());
        assertEquals("marjetbosma", ownerUsernameArgumentCaptor.getValue());
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
