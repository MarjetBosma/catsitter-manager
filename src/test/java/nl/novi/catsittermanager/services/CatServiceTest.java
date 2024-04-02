package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatRequestFactory;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
    CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

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

    @Test
    void testGetAllCats_noCatsInDatabase_shouldReturnEmptyList() {
        // Given
        when(catRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Cat> catResponseList = catService.getAllCats();

        // Then
        assertTrue(catResponseList.isEmpty());
        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);
    }


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

    @Test
    void testCreateCat_shouldCreateANewCat() {
        // Given
        Cat expectedCat = CatFactory.randomCat().build();
        Customer customer = CustomerFactory.randomCustomer();

        when(customerService.getCustomer(customer.getUsername())).thenReturn(customer);
        when(catRepository.save(expectedCat)).thenReturn(expectedCat);

        // When
        Cat resultCat = catService.createCat(expectedCat, customer.getUsername());

        // Then
        assertEquals(expectedCat, resultCat);

        verify(customerService, times(1)).getCustomer(customer.getUsername());
        verifyNoMoreInteractions(customerService);

        verify(catRepository, times(1)).save(expectedCat);
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testEditCat_shouldEditExistingCat() {
        // Given
        UUID catId = UUID.randomUUID();
        Cat existingCat = CatFactory.randomCat().build();
        CatRequest updatedCatRequest = CatRequestFactory.randomCatRequest().build();

        when(catRepository.findById(catId)).thenReturn(Optional.of(existingCat));
        // when(customerRepository.findById(updatedCatRequest.ownerUsername())).thenReturn(Optional.of(CustomerFactory.randomCustomer().build())); // Waarom werkt dit niet?
        when(catRepository.save(existingCat)).thenReturn(existingCat);

        // When
        CatResponse resultCat = catService.editCat(catId, updatedCatRequest);

        // Then
        assertEquals(existingCat.getId(), resultCat.id());
        assertEquals(updatedCatRequest.name(), resultCat.name());
        assertEquals(updatedCatRequest.dateOfBirth(), resultCat.dateOfBirth());
        assertEquals(updatedCatRequest.gender(), resultCat.gender());
        assertEquals(updatedCatRequest.breed(), resultCat.breed());
        assertEquals(updatedCatRequest.generalInfo(), resultCat.generalInfo());
        assertEquals(updatedCatRequest.spayedOrNeutered(), resultCat.spayedOrNeutered());
        assertEquals(updatedCatRequest.vaccinated(), resultCat.vaccinated());
        assertEquals(updatedCatRequest.veterinarianName(), resultCat.veterinarianName());
        assertEquals(updatedCatRequest.phoneVet(), resultCat.phoneVet());
        assertEquals(updatedCatRequest.medicationName(), resultCat.medicationName());
        assertEquals(updatedCatRequest.medicationDose(), resultCat.medicationDose());
//        assertEquals(updatedCatRequest.ownerUsername(), resultCat.ownerUsername()); // hier werkt het ook niet


        verify(catRepository, times(1)).findById(catId);
        verify(customerRepository, times(1)).findById(updatedCatRequest.ownerUsername());
        verify(catRepository, times(1)).save(existingCat);
        verifyNoMoreInteractions(catRepository);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testEditCat_existingCat_shouldEditAttributes() {
        // Given
        UUID catId = UUID.randomUUID();
        Cat existingCat = CatFactory.randomCat().build();
        CatRequest updatedCatRequest = CatRequestFactory.randomCatRequest().build();

        when(catRepository.findById(catId)).thenReturn(Optional.of(existingCat));
        when(catRepository.save(existingCat)).thenReturn(existingCat);

        // When
        CatResponse resultCat = catService.editCat(catId, updatedCatRequest);

        // Then
        assertEquals(updatedCatRequest.name(), resultCat.name());
        assertEquals(updatedCatRequest.dateOfBirth(), resultCat.dateOfBirth());
        assertEquals(updatedCatRequest.dateOfBirth(), resultCat.dateOfBirth());
        assertEquals(updatedCatRequest.gender(), resultCat.gender());
        assertEquals(updatedCatRequest.breed(), resultCat.breed());
        assertEquals(updatedCatRequest.generalInfo(), resultCat.generalInfo());
        assertEquals(updatedCatRequest.spayedOrNeutered(), resultCat.spayedOrNeutered());
        assertEquals(updatedCatRequest.vaccinated(), resultCat.vaccinated());
        assertEquals(updatedCatRequest.veterinarianName(), resultCat.veterinarianName());
        assertEquals(updatedCatRequest.phoneVet(), resultCat.phoneVet());
        assertEquals(updatedCatRequest.medicationName(), resultCat.medicationName());
        assertEquals(updatedCatRequest.medicationDose(), resultCat.medicationDose());
        // assertEquals(updatedCatRequest.ownerUsername(), resultCat.ownerUsername()); // owner geeft ook hier problemen
    }

    @Test
    void testEditCat_nonExistingCat_shouldThrowRecordNotFoundException() {
        // Given
        UUID nonExistingCatId = UUID.randomUUID();
        CatRequest updatedCatRequest = CatRequestFactory.randomCatRequest().build();

        when(catRepository.findById(nonExistingCatId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> catService.editCat(nonExistingCatId, updatedCatRequest));
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testEditCat_updateName_shouldChangeName() {
        // Given
        UUID catId = UUID.randomUUID();
        Cat existingCat = CatFactory.randomCat().build();
        CatRequest updatedCatRequest = CatRequestFactory.randomCatRequest().name("New Name").build();

        when(catRepository.findById(catId)).thenReturn(Optional.of(existingCat));
        when(catRepository.save(existingCat)).thenReturn(existingCat);

        // When
        CatResponse resultCat = catService.editCat(catId, updatedCatRequest);

        // Then
        assertEquals("New Name", resultCat.name());
    } // geeft ook weer error owner not found

    // todo: Dezelfde tests schrijven voor andere attributen, wanneer de fout uit bovenstaande is gehaald.

    @Test
    void testDeleteCat_ShouldDeleteCatWithSpecificId() {
        // Given
        UUID catId = UUID.randomUUID();

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.deleteCat(catId));

        // Then
        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository, times(1)).existsById(catId);
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testDeleteCat_shouldDeleteCatWithSpecificId_RecordNotFoundException() {
        // Given
        UUID catId = UUID.randomUUID();
        when(catRepository.existsById(catId)).thenReturn(false);

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.deleteCat(catId));

        // Then
        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository, never()).deleteById(catId);
        verifyNoMoreInteractions(catRepository);
    }
}

