package nl.novi.catsittermanager.services;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepository;
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CatService catService;

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
        Cat cat = CatFactory.randomCat().build();

        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        when(customerService.getCustomer(cat.getOwner().getUsername())).thenReturn(cat.getOwner());
        when(catRepository.save(cat)).thenReturn(cat);

        // When
        Cat resultCat = catService.editCat(cat.getId(), cat, cat.getOwner().getUsername());

        // Then
        assertEquals(cat, resultCat);

        verify(catRepository, times(1)).findById(cat.getId());
        verify(customerService, times(1)).getCustomer(cat.getOwner().getUsername());
        verify(catRepository, times(1)).save(cat);
        verifyNoMoreInteractions(catRepository);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testEditCat_nonExistingCat_shouldThrowRecordNotFoundException() {
        // Given
        Cat cat = CatFactory.randomCat().build();

        when(catRepository.findById(cat.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> catService.editCat(cat.getId(), cat, cat.getOwner().getUsername()));
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testDeleteCat_ShouldDeleteCatWithSpecificId() {
        // Given
        Cat cat = CatFactory.randomCat().build();
        when(catRepository.existsById(cat.getId())).thenReturn(true);

        // When
        UUID catId = catService.deleteCat(cat.getId());

        // Then
        verify(catRepository, times(1)).existsById(catId);
        verify(catRepository, times(1)).deleteById(catId);
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

