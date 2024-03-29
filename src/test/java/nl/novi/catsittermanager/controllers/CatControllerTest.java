package nl.novi.catsittermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.services.CatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatController.class)
class CatControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatService catService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllCats() {
    }

    @Test
    void givenAValidRequest_whenGetAllCats_thenAllCatsShouldBeReturned() throws Exception {
        // given
        Cat expetedCat = CatFactory.randomCat().build();
        List<Cat> expetedCatList = List.of(expetedCat);

        when(catService.getAllCats()).thenReturn(expetedCatList);

        mockMvc.perform(get("/cat")
                        .with(SecurityMockMvcRequestPostProcessors.user("diffblue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expetedCatList.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expetedCat.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expetedCat.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].dateOfBirth").value(expetedCat.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].gender").value(expetedCat.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].generalInfo").value(expetedCat.getGeneralInfo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].spayedOrNeutered").value(expetedCat.getSpayedOrNeutered().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].vaccinated").value(expetedCat.getVaccinated().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].veterinarianName").value(expetedCat.getVeterinarianName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].phoneVet").value(expetedCat.getPhoneVet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].medicationName").value(expetedCat.getMedicationName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].medicationDose").value(expetedCat.getMedicationDose()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].ownerUsername").value(expetedCat.getOwner().getUsername()));
    }

    //todo not happyflow tests for getAllCats

    @Test
    void givenAValidRequest_whenGetCat_thenCatShouldBeReturned() throws Exception {
        // given
        Cat expetedCat = CatFactory.randomCat().build();

        when(catService.getCat(expetedCat.getId())).thenReturn(expetedCat);

        mockMvc.perform(get("/cat/" + expetedCat.getId().toString())
                        .with(SecurityMockMvcRequestPostProcessors.user("diffblue"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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

    //todo not happyflow tests for getAllCats
}