package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatRequestFactory;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.services.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatController.class)
@ComponentScan({"nl.novi.catsittermanager.utils"})
class CatControllerTest {

    MockMvc mockMvc;
    @MockBean
    CatService catService;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() {

        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser("user")
    void givenAValidRequest_whenGetAllCats_thenAllCatsShouldBeReturned() throws Exception {
        // given
        Cat expectedCat = CatFactory.randomCat().build();
        List<Cat> expectedCatList = List.of(expectedCat);

        when(catService.getAllCats()).thenReturn(expectedCatList);

        mockMvc.perform(get("/cat")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedCatList.size()))
                .andExpect(jsonPath("$.[0].id").value(expectedCat.getId().toString()))
                .andExpect(jsonPath("$.[0].name").value(expectedCat.getName()))
                .andExpect(jsonPath("$.[0].dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.[0].gender").value(expectedCat.getGender()))
                .andExpect(jsonPath("$.[0].generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(jsonPath("$.[0].spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(jsonPath("$.[0].vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(jsonPath("$.[0].veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(jsonPath("$.[0].phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(jsonPath("$.[0].medicationName").value(expectedCat.getMedicationName()))
                .andExpect(jsonPath("$.[0].medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(jsonPath("$.[0].ownerUsername").value(expectedCat.getOwner().getUsername()));
    }

    @Test
    @WithMockUser("user")
    void givenNoCatsAvailable_whenGetAllCats_thenEmptyListShouldBeReturned() throws Exception {
        // given
        when(catService.getAllCats()).thenReturn(Collections.emptyList());

        // perform the GET request to fetch all cats
        mockMvc.perform(get("/cat")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser("user")
    void givenAValidRequest_whenGetCat_thenCatShouldBeReturned() throws Exception {

        Cat expectedCat = CatFactory.randomCat().build();

        when(catService.getCat(expectedCat.getId())).thenReturn(expectedCat);

        mockMvc.perform(get("/cat/" + expectedCat.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCat.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedCat.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(expectedCat.getGender()))
                .andExpect(jsonPath("$.generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(jsonPath("$.spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(jsonPath("$.vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(jsonPath("$.veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(jsonPath("$.phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(jsonPath("$.medicationName").value(expectedCat.getMedicationName()))
                .andExpect(jsonPath("$.medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(jsonPath("$.ownerUsername").value(expectedCat.getOwner().getUsername()));
    }

    @Test
    @WithMockUser("user")
    void givenInvalidCatId_whenGetCat_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        UUID invalidCatId = UUID.randomUUID();
        final String errorMessage = "No cat found with this id.";

        when(catService.getCat(invalidCatId)).thenThrow(new RecordNotFoundException(errorMessage));

        mockMvc.perform(get("/cat/" + invalidCatId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

    @Test
    @WithMockUser("user")
    void givenAValidRequest_whenCreateCat_thenCatShouldBeReturned() throws Exception {

        CatRequest expectedCatRequest = CatRequestFactory.randomCatRequest().build();
        Cat expectedCat = CatFactory.randomCat().build();

        when(catService.createCat(any(Cat.class), eq(expectedCatRequest.ownerUsername()))).thenReturn(expectedCat);

        mockMvc.perform(post("/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCatRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedCat.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedCat.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(expectedCat.getGender()))
                .andExpect(jsonPath("$.generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(jsonPath("$.spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(jsonPath("$.vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(jsonPath("$.veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(jsonPath("$.phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(jsonPath("$.medicationName").value(expectedCat.getMedicationName()))
                .andExpect(jsonPath("$.medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(jsonPath("$.ownerUsername").value(expectedCat.getOwner().getUsername()));
    }

    @Test
    void givenInvalidData_whenCreateCat_thenBadRequest() throws Exception {
        CatRequest invalidCatRequest = CatRequestFactory.randomCatRequest()
                .name(null)
                .dateOfBirth(null)
                .gender(null)
                .breed(null)
                .generalInfo(null)
                .spayedOrNeutered(null)
                .vaccinated(null)
                .veterinarianName(null)
                .phoneVet(null)
                .medicationName(null)
                .medicationDose(null)
                .ownerUsername(null)
                .build();

        mockMvc.perform(post("/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCatRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(catService);
    }

    @Test
    void givenValidId_whenEditCat_thenCatShouldBeEdited() throws Exception {

        UUID catId = UUID.randomUUID();
        CatRequest expectedCatRequest = CatRequestFactory.randomCatRequest().build();
        Cat expectedCat = CatFactory.randomCat().build();

        when(catService.editCat(eq(catId), any(Cat.class), eq(expectedCatRequest.ownerUsername()))).thenReturn(expectedCat);

        mockMvc.perform(MockMvcRequestBuilders.put("/cat/{id}", catId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCatRequest)))
                .andExpect(status().isOk());

        verify(catService, times(1)).editCat(eq(catId), any(Cat.class), eq(expectedCatRequest.ownerUsername()));
        verifyNoMoreInteractions(catService);
    }

    @Test
    void givenInvalidId_whenEditCat_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {

        UUID catId = UUID.randomUUID();
        CatRequest expectedCatRequest = CatRequestFactory.randomCatRequest().build();

        when(catService.editCat(eq(catId), any(Cat.class), eq(expectedCatRequest.ownerUsername())))
                .thenThrow(new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id."));

        mockMvc.perform(MockMvcRequestBuilders.put("/cat/{id}", catId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCatRequest)))
                .andExpect(status().isNotFound());
    }

    void givenInvalidData_whenEditCat_thenBadRequest() throws Exception {
        UUID invalidCatId = UUID.randomUUID();

        CatRequest invalidCatRequest = CatRequestFactory.randomCatRequest()
                .name(null)
                .dateOfBirth(null)
                .gender(null)
                .breed(null)
                .generalInfo(null)
                .spayedOrNeutered(null)
                .vaccinated(null)
                .veterinarianName(null)
                .phoneVet(null)
                .medicationName(null)
                .medicationDose(null)
                .ownerUsername(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/cat/{id}", invalidCatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCatRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(catService);
    }

    @Test
    void givenValidId_whenDeleteCat_thenCatShouldBeDeleted() throws Exception {
        UUID catId = UUID.randomUUID();

        when(catService.deleteCat(catId)).thenReturn(catId);

        mockMvc.perform(delete("/cat/{id}", catId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cat with id " + catId + " removed from database."));
    }

    @Test
    void givenInvalidId_whenDeleteCat_thenRecordNotFoundExceptionShouldBeThrown() throws Exception {
        UUID invalidCatId = UUID.randomUUID();
        final String errorMessage = "error";

        when(catService.deleteCat(invalidCatId)).thenThrow(new RecordNotFoundException(errorMessage));

        mockMvc.perform(delete("/cat/{id}", invalidCatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(errorMessage));
    }

}