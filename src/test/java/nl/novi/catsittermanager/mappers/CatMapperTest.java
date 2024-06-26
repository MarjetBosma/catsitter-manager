package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.CatRequestFactory;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatMapperTest {

    @Test
    void testCatToCatResponse() {

        // Arrange
        Cat cat = CatFactory.randomCat().build();

        // Act
        CatResponse catResponse = CatMapper.CatToCatResponse(cat);

        // Assert
        assertEquals(cat.getId(), catResponse.id());
        assertEquals(cat.getName(), catResponse.name());
        assertEquals(cat.getDateOfBirth().toString(), catResponse.dateOfBirth());
        assertEquals(cat.getGender(), catResponse.gender());
        assertEquals(cat.getBreed(), catResponse.breed());
        assertEquals(cat.getGeneralInfo(), catResponse.generalInfo());
        assertEquals(cat.getSpayedOrNeutered(), catResponse.spayedOrNeutered());
        assertEquals(cat.getVaccinated(), catResponse.vaccinated());
        assertEquals(cat.getVeterinarianName(), catResponse.veterinarianName());
        assertEquals(cat.getPhoneVet(), catResponse.phoneVet());
        assertEquals(cat.getMedicationName(), catResponse.medicationName());
        assertEquals(cat.getMedicationDose(), catResponse.medicationDose());
        assertEquals(cat.getOwner().getUsername(), catResponse.ownerUsername());
    }

    @Test
    void testCatRequestToCat() {

        // Arrange
        CatRequest catRequest = CatRequestFactory.randomCatRequest().build();

        // When
        Cat cat = CatMapper.CatRequestToCat(catRequest);

        // Then
        assertEquals(catRequest.name(), cat.getName());
        assertEquals(LocalDate.parse(catRequest.dateOfBirth()), cat.getDateOfBirth());
        assertEquals(catRequest.gender(), cat.getGender());
        assertEquals(catRequest.breed(), cat.getBreed());
        assertEquals(catRequest.generalInfo(), cat.getGeneralInfo());
        assertEquals(catRequest.spayedOrNeutered(), cat.getSpayedOrNeutered());
        assertEquals(catRequest.vaccinated(), cat.getVaccinated());
        assertEquals(catRequest.veterinarianName(), cat.getVeterinarianName());
        assertEquals(catRequest.phoneVet(), cat.getPhoneVet());
        assertEquals(catRequest.medicationName(), cat.getMedicationName());
        assertEquals(catRequest.medicationDose(), cat.getMedicationDose());
        assertEquals(catRequest.ownerUsername(),  cat.getOwner().getUsername());
    }
}


