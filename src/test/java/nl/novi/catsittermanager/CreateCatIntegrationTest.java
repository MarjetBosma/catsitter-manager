package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.services.CatService;
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
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCat() throws Exception {
        String jsonInput = """
                {
                "name": "Firsa",
                "dateOfBirth": "2006-07-01",
                "gender": "V",
                "breed": "Europese Korthaar",
                "generalInfo": "Vriendelijke, maar verlegen kat. Verstopt zich vaak voor vreemden.",
                "spayedOrNeutered": true,
                "vaccinated": true,
                "veterinarianName": "Dierenkliniek Zuilen",
                "phoneVet": "030-123456",
                "medicationName": "Semintra",
                "medicationDose": "2ml 1dd",
                "ownerUsername": "marjetbosma"
                 }
                  """;

        ArgumentCaptor<Cat> catArgumentCaptor = ArgumentCaptor.forClass(Cat.class);
        ArgumentCaptor<String> ownerUsernameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        when(catRepository.save(any(Cat.class))).thenReturn(new Cat());
        when(catRepository.save(any(Cat.class))).thenThrow(new DataIntegrityViolationException("Invalid cat data"));

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String createdId = jsonNode.get("id").asText();

        verify(catRepository, times(1)).save(catArgumentCaptor.capture());
        verifyNoMoreInteractions(catRepository);

        verify(catService, times(1)).createCat(catArgumentCaptor.getValue(), ownerUsernameArgumentCaptor.capture());
        assertEquals("marjetbosma", ownerUsernameArgumentCaptor.getValue());

        MatcherAssert.assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/cat" + createdId));
    }
}
