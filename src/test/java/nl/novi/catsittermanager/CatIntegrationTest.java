package nl.novi.catsittermanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.matchesPattern;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test") // is voor tijdelijke database
class CatIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

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
                "generalInfo": "Vriendelijke , maar verlegen kat. Verstopt zich vaak voor vreemden.",
                "spayedOrNeutered": true,
                "vaccinated": true,
                "veterinarianName": "Dierenkliniek Zuilen",
                "phoneVet": "030-123456",
                "medicationName": "Semintra",
                "medicationDose": "2ml 1dd",
                "ownerUsername": "marjetbosma"
                 }
                  """;
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/televisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String createdId = jsonNode.get("id").asText();

        MatcherAssert.assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/cat" + createdId));
    }
}

// h2 database in configuratie
// mockmvc voor requests sturen en valideren
// argument captor, verify, spy voor service methods valideren
// verifyNoMoreInteractions
// test entity manager gebruiken

// bijv. wat gebeurt er als er al data aanwezig is