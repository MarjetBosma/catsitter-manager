package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepository;

    @Mock
    private CatMapper catMapper;

    @Mock
    private CustomerRepository customerRepos;

    @InjectMocks
    private CatService catService;

    //TODO fix test
    @Test
    void testGetAllCats_shouldFetchAllCatsOnTheList() {
        // Given
        Cat expectedCat = CatFactory.randomCat().build();
        List<Cat> expectedCatList = List.of(expectedCat);

        when(catRepository.findAll()).thenReturn(expectedCatList);

        // When
        List<Cat> catResponseList = catService.getAllCats();

        // Then
        assertEquals(expectedCatList, catResponseList);
        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);
    }

    //Todo Test unhappy flow for getAllCats

    @Test
    void testGetCat_shouldFetchCatWithSpecificId() {
        // Given
        Cat expectedCat = CatFactory.randomCat().build();

        when(catRepository.findById(expectedCat.getId())).thenReturn(Optional.of(expectedCat));

        // When
        Cat resultCat = catService.getCat(expectedCat.getId());

        // Then
        assertEquals(expectedCat, resultCat);
        verify(catRepository, times(1)).findById(expectedCat.getId());
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId_RecordNotFoundException() {
        // Given
        Cat expectedCat = CatFactory.randomCat().build();

        when(catRepository.findById(expectedCat.getId())).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.getCat(expectedCat.getId()));

        // Then
        assertEquals("No cat found with this id.", exception.getMessage());
    }

//    @Test
//    void testCreateCat_newCatObjectShouldBeCreated_fieldsInCatInputDtoAndCatDtoShouldContainTheSameValues() {
//
//        //Arrange
//        Customer owner = new Customer();
//        owner.setUsername("marjet_bosma");
//        Cat expectedCat = new Cat();
//        expectedCat.setId(UUID.randomUUID());
//
//        when(customerRepos.findById(owner.getUsername())).thenReturn(Optional.of(owner));
//
//        CatInputDto catInputDto = new CatInputDto("Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", "marjet_bosma");
//        when(catMapper.transferFromInputDto(any())).thenReturn(expectedCat);
//        when(catMapper.CatToCatResponse(any())).thenReturn(new CatResponse(expectedCat.getId(), "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", "marjet_bosma"));
//
//        // Act
//        CatResponse result = catService.createCat(catInputDto);
//
//        // Assert
//
//        verify(catRepository, times(1)).save(expectedCat);
//        verify(customerRepos, times(1)).findById(owner.getUsername());
//        assertEquals(expectedCat.getId(), result.id());
//        assertEquals("Firsa", result.name());
//        assertEquals(LocalDate.of(2006, 7, 1), result.dateOfBirth());
//        assertEquals("V", result.gender());
//        assertEquals("Europese Korthaar", result.breed());
//        assertEquals("Vriendelijke, maar verlegen kat", result.generalInfo());
//        assertEquals(true, result.spayedOrNeutered());
//        assertEquals(true, result.vaccinated());
//        assertEquals("Dierenkliniek Zuilen", result.veterinarianName());
//        assertEquals("030-2446933", result.phoneVet());
//        assertEquals("Thiafeline", result.medicationName());
//        assertEquals("5mg 1dd", result.medicationDose());
//        assertEquals("marjet_bosma", result.ownerUsername());
//    }

//    @Test
//    void testEditCat_editedCatObjectShouldBeReturned_editedFieldsShouldContainNewValues() {
//        // Arrange
//        UUID catId = UUID.randomUUID();
//        Customer owner = new Customer();
//        owner.setUsername("marjet_bosma");
//
//        Cat existingCat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", null);
//
//        CatInputDto updatedCatInputDto = new CatInputDto("UpdatedName", LocalDate.of(2010, 5, 15), "M", "UpdatedBreed", "UpdatedInfo", false, false, "UpdatedVet", "123456789", "UpdatedMed", "10mg 2dd", "marjet_bosma");
//
//        // Mock behavior of repository
//        when(catRepository.findById(catId)).thenReturn(Optional.of(existingCat));
//        when(customerRepos.findById(owner.getUsername())).thenReturn(Optional.of(owner));
//        when(customerRepos.findById(updatedCatInputDto.ownerUsername())).thenReturn(Optional.empty());
//
//        // Mock behavior of mapper
//        when(catMapper.CatToCatResponse(any())).thenAnswer(invocation -> {
//            Cat catArgument = invocation.getArgument(0);
//            return new CatResponse(catArgument.getId(), catArgument.getName(), catArgument.getDateOfBirth(), catArgument.getGender(),
//                    catArgument.getBreed(), catArgument.getGeneralInfo(), catArgument.getSpayedOrNeutered(), catArgument.getVaccinated(),
//                    catArgument.getVeterinarianName(), catArgument.getPhoneVet(), catArgument.getMedicationName(), catArgument.getMedicationDose(),
//                    catArgument.getOwner() != null ? catArgument.getOwner().getUsername() : null);
//        });
//
//        // Act
//        CatResponse result = catService.editCat(catId, updatedCatInputDto);
//
//        // Assert
//        assertEquals("UpdatedName", result.name());
//        assertEquals(LocalDate.of(2010, 5, 15), result.dateOfBirth());
//        assertEquals("M", result.gender());
//        assertEquals("UpdatedBreed", result.breed());
//        assertEquals("UpdatedInfo", result.generalInfo());
//        assertFalse(result.spayedOrNeutered());
//        assertFalse(result.vaccinated());
//        assertEquals("UpdatedVet", result.veterinarianName());
//        assertEquals("123456789", result.phoneVet());
//        assertEquals("UpdatedMed", result.medicationName());
//        assertEquals("10mg 2dd", result.medicationDose());
//    }

//    @Test
//    void testEditCat_editedCatObjectShouldBeReturned_RecordNotFoundException() {
//        UUID catId = UUID.randomUUID();
//        Customer owner = new Customer();
//        owner.setUsername("marjet_bosma");
//
//        // Existing Cat
//        Cat existingCat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", null);
//
//        // Updated CatInputDto
//        CatInputDto updatedCatInputDto = new CatInputDto("UpdatedName", LocalDate.of(2010, 5, 15), "M", "UpdatedBreed", "UpdatedInfo", false, false, "UpdatedVet", "123456789", "UpdatedMed", "10mg 2dd", "marjet_bosma");
//
//        // Mock behavior of repository
//        when(catRepository.findById(catId)).thenReturn(Optional.of(existingCat));
//        when(customerRepos.findById(updatedCatInputDto.ownerUsername())).thenReturn(Optional.empty());
//
//        // Act and assert
//        RecordNotFoundException result = assertThrows(RecordNotFoundException.class, () -> catService.editCat(catId, updatedCatInputDto));
//        assertEquals("Owner not found", result.getMessage());
//    }

//    @Test
//    void testDeleteCat_catObjectShouldBeDeletedFromDatabase() {
//        // Arrange
//        UUID catIdToDelete = UUID.randomUUID();
//
//        // Act
//        UUID deletedCatId = catService.deleteCat(catIdToDelete);
//
//        // Assert
//        verify(catRepository, times(1)).deleteById(catIdToDelete);
//        assertEquals(catIdToDelete, deletedCatId);
//    }
}
