package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatRequestFactory;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.services.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatController.class)
class CatControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    CatService catService;

    ObjectMapper objectMapper = new ObjectMapper();

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expectedCatList.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedCat.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expectedCat.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].gender").value(expectedCat.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].medicationName").value(expectedCat.getMedicationName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].ownerUsername").value(expectedCat.getOwner().getUsername()));
    }

    //todo not happyflow tests for getAllCats

    @Test
    @WithMockUser("user")
    void givenAValidRequest_whenGetCat_thenCatShouldBeReturned() throws Exception {
        // given
        Cat expectedCat = CatFactory.randomCat().build();

        when(catService.getCat(expectedCat.getId())).thenReturn(expectedCat);

        mockMvc.perform(get("/cat/" + expectedCat.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedCat.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedCat.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(expectedCat.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(expectedCat.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.generalInfo").value(expectedCat.getGeneralInfo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.spayedOrNeutered").value(expectedCat.getSpayedOrNeutered().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vaccinated").value(expectedCat.getVaccinated().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.veterinarianName").value(expectedCat.getVeterinarianName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneVet").value(expectedCat.getPhoneVet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicationName").value(expectedCat.getMedicationName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicationDose").value(expectedCat.getMedicationDose()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerUsername").value(expectedCat.getOwner().getUsername()));
    }

    //todo not happyflow tests for getAllCats

    @Test
    @WithMockUser("user")
    void givenAValidRequest_whenCreateCat_thenCatShouldBeReturned() throws Exception {
        // given
        CatRequest expetedCatRequest = CatRequestFactory.randomCatRequest().build();
        Cat expetedCat = CatFactory.randomCat().build();

        when(catService.createCat(any(Cat.class), eq(expetedCatRequest.ownerUsername()))).thenReturn(expetedCat);

        mockMvc.perform(post("/cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expetedCatRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expetedCat.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expetedCat.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(expetedCat.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(expetedCat.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.generalInfo").value(expetedCat.getGeneralInfo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.spayedOrNeutered").value(expetedCat.getSpayedOrNeutered().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vaccinated").value(expetedCat.getVaccinated().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.veterinarianName").value(expetedCat.getVeterinarianName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneVet").value(expetedCat.getPhoneVet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicationName").value(expetedCat.getMedicationName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicationDose").value(expetedCat.getMedicationDose()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerUsername").value(expetedCat.getOwner().getUsername()));
    }

    //todo not happyflow tests for createCat
}