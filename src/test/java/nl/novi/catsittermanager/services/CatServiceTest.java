package nl.novi.catsittermanager.services;

import net.bytebuddy.dynamic.DynamicType;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepos;

    @Mock
    private CatMapper catMapper;

    @Mock
    private CustomerRepository customerRepos;

    @InjectMocks
    private CatService catService;

    @Test
    void testGetAllCats_shouldFetchAllCatsOnTheList() {

        // Arrange
        Customer owner1 = new Customer();
        owner1.setUsername("marjet_bosma");

        Customer owner2 = new Customer();
        owner2.setUsername("marianne_bosma");

        List<Cat> catList = new ArrayList<>();
        catList.add(new Cat(UUID.randomUUID(), "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1));
        catList.add(new Cat(UUID.randomUUID(), "Kees", LocalDate.of(2009, 9, 1), "M", "Europese Korthaar", "Kan onverwacht agressief worden", true, true, "Dierenkliniek Hoevelaken", "038-4567891", null, null, owner2));

        // Mock behavior of the repository
        when(catRepos.findAll()).thenReturn(catList);

        // Act
        List<CatDto> catDtoList = catService.getAllCats();

        // Assert
        assertEquals(2, catDtoList.size());
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId() {
        // Arrange
        Customer owner1 = new Customer();
        owner1.setUsername("marjet_bosma");

        UUID catId = UUID.randomUUID();
        Cat cat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1);

        CatDto expectedCatDto = new CatDto(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1.getUsername());

        // Mock behavior of the repository
        when(catRepos.findById(catId)).thenReturn(Optional.of(cat));

        // Mock behavior of the mapper
        when(catMapper.transferToDto(cat)).thenReturn(expectedCatDto);

        // Act
        CatDto actualCatDto = catService.getCat(catId);

        // Assert
        assertEquals(expectedCatDto, actualCatDto);
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId_RecordNotFoundException() {
        // Arrange
        UUID catId = UUID.randomUUID();

        // Mock behavior of the repository
        when(catRepos.findById(catId)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException result = assertThrows(RecordNotFoundException.class, () -> catService.getCat(catId));

        // Assert
        assertEquals("No cat found with this id.", result.getMessage());
    }

    @Test
    void testCreateCat_newCatObjectShouldBeCreated_fieldsInCatInputDtoAndCatDtoShouldContainTheSameValues() {

        //Arrange
        Customer owner = new Customer();
        owner.setUsername("marjet_bosma");
        Cat expectedCat = new Cat();
        expectedCat.setId(UUID.randomUUID());

        when(customerRepos.findById(owner.getUsername())).thenReturn(Optional.of(owner));

        CatInputDto catInputDto = new CatInputDto("Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", "marjet_bosma");
        when(catMapper.transferFromInputDto(any())).thenReturn(expectedCat);
        when(catMapper.transferToDto(any())).thenReturn(new CatDto(expectedCat.getId(), "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", "marjet_bosma"));

        // Act
        CatDto result = catService.createCat(catInputDto);

        // Assert

        verify(catRepos, times(1)).save(expectedCat);
        verify(customerRepos, times(1)).findById(owner.getUsername());
        assertEquals(expectedCat.getId(), result.id());
        assertEquals("Firsa", result.name());
        assertEquals(LocalDate.of(2006, 7, 1), result.dateOfBirth());
        assertEquals("V", result.gender());
        assertEquals("Europese Korthaar", result.breed());
        assertEquals("Vriendelijke, maar verlegen kat", result.generalInfo());
        assertEquals(true, result.spayedOrNeutered());
        assertEquals(true, result.vaccinated());
        assertEquals("Dierenkliniek Zuilen", result.veterinarianName());
        assertEquals("030-2446933", result.phoneVet());
        assertEquals("Thiafeline", result.medicationName());
        assertEquals("5mg 1dd", result.medicationDose());
        assertEquals("marjet_bosma", result.ownerUsername());
    }

    @Test
    void testEditCat_editedCatObjectShouldBeReturned_editedFieldsShouldContainNewValues() {
        // Arrange
        UUID catId = UUID.randomUUID();
        Customer owner = new Customer();
        owner.setUsername("marjet_bosma");

        Cat existingCat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", null);

        CatInputDto updatedCatInputDto = new CatInputDto("UpdatedName", LocalDate.of(2010, 5, 15), "M", "UpdatedBreed", "UpdatedInfo", false, false, "UpdatedVet", "123456789", "UpdatedMed", "10mg 2dd", "marjet_bosma");

        // Mock behavior of repository
        when(catRepos.findById(catId)).thenReturn(Optional.of(existingCat));
        when(customerRepos.findById(owner.getUsername())).thenReturn(Optional.of(owner));
        when(customerRepos.findById(updatedCatInputDto.ownerUsername())).thenReturn(Optional.empty());

        // Mock behavior of mapper
        when(catMapper.transferToDto(any())).thenAnswer(invocation -> {
            Cat catArgument = invocation.getArgument(0);
            return new CatDto(catArgument.getId(), catArgument.getName(), catArgument.getDateOfBirth(), catArgument.getGender(),
                    catArgument.getBreed(), catArgument.getGeneralInfo(), catArgument.getSpayedOrNeutered(), catArgument.getVaccinated(),
                    catArgument.getVeterinarianName(), catArgument.getPhoneVet(), catArgument.getMedicationName(), catArgument.getMedicationDose(),
                    catArgument.getOwner() != null ? catArgument.getOwner().getUsername() : null);
        });

        // Act
        CatDto result = catService.editCat(catId, updatedCatInputDto);

        // Assert
        assertEquals("UpdatedName", result.name());
        assertEquals(LocalDate.of(2010, 5, 15), result.dateOfBirth());
        assertEquals("M", result.gender());
        assertEquals("UpdatedBreed", result.breed());
        assertEquals("UpdatedInfo", result.generalInfo());
        assertFalse(result.spayedOrNeutered());
        assertFalse(result.vaccinated());
        assertEquals("UpdatedVet", result.veterinarianName());
        assertEquals("123456789", result.phoneVet());
        assertEquals("UpdatedMed", result.medicationName());
        assertEquals("10mg 2dd", result.medicationDose());
    }

    @Test
    void testEditCat_editedCatObjectShouldBeReturned_RecordNotFoundException() {
        UUID catId = UUID.randomUUID();
        Customer owner = new Customer();
        owner.setUsername("marjet_bosma");

        // Existing Cat
        Cat existingCat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", null);

        // Updated CatInputDto
        CatInputDto updatedCatInputDto = new CatInputDto("UpdatedName", LocalDate.of(2010, 5, 15), "M", "UpdatedBreed", "UpdatedInfo", false, false, "UpdatedVet", "123456789", "UpdatedMed", "10mg 2dd", "marjet_bosma");

        // Mock behavior of repository
        when(catRepos.findById(catId)).thenReturn(Optional.of(existingCat));
        when(customerRepos.findById(updatedCatInputDto.ownerUsername())).thenReturn(Optional.empty());

        // Act and assert
        RecordNotFoundException result = assertThrows(RecordNotFoundException.class, () -> catService.editCat(catId, updatedCatInputDto));
        assertEquals("Owner not found", result.getMessage());
    }

    @Test
    void testDeleteCat_catObjectShouldBeDeletedFromDatabase() {
        // Arrange
        UUID catIdToDelete = UUID.randomUUID();

        // Act
        UUID deletedCatId = catService.deleteCat(catIdToDelete);

        // Assert
        verify(catRepos, times(1)).deleteById(catIdToDelete);
        assertEquals(catIdToDelete, deletedCatId);
    }
}
