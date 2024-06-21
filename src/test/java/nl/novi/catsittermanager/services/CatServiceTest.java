package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import nl.novi.catsittermanager.repositories.CatRepository;
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

        // Arrange
        Cat expectedCat = CatFactory.randomCat().build();
        List<Cat> expectedCatList = List.of(expectedCat);

        when(catRepository.findAll()).thenReturn(expectedCatList);

        // Act
        List<Cat> catResponseList = catService.getAllCats();

        // Assert
        assertEquals(expectedCatList, catResponseList);
        verify(catRepository, times(1)).findAll();
        verifyNoInteractions(customerService);
    }

    @Test
    void testGetAllCats_noCatsInDatabase_shouldReturnEmptyList() {

        // Arrange
        when(catRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Cat> catResponseList = catService.getAllCats();

        // Assert
        assertTrue(catResponseList.isEmpty());
        verify(catRepository, times(1)).findAll();
        verifyNoInteractions(customerService);
    }

    @Test
    void testEditCat_withOwner_shouldEditCatWithOwnerPresent() {
        
        // Arrange
        Cat cat = CatFactory.randomCat().build();
        Customer newOwner = CustomerFactory.randomCustomer().build();
        String ownerUsername = "newOwnerUsername";

        cat.setOwner(newOwner);

        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        when(customerService.getCustomer(ownerUsername)).thenReturn(newOwner);
        when(catRepository.save(any(Cat.class))).thenReturn(cat);

        // Act
        Cat resultCat = catService.editCat(cat.getId(), cat, ownerUsername);

        // Assert
        assertEquals(newOwner, resultCat.getOwner());
        verify(catRepository, times(1)).findById(cat.getId());
        verify(customerService, times(1)).getCustomer(ownerUsername);
        verify(catRepository, times(1)).save(any(Cat.class));
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId() {

        // Arrange
        Cat expectedCat = CatFactory.randomCat().build();

        when(catRepository.findById(expectedCat.getId())).thenReturn(Optional.of(expectedCat));

        // Act
        Cat resultCat = catService.getCat(expectedCat.getId());

        // Assert
        assertEquals(expectedCat, resultCat);
        verify(catRepository, times(1)).findById(expectedCat.getId());
        verifyNoInteractions(customerService);
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID catId = UUID.randomUUID();
        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.getCat(catId));

        // Assert
        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository).findById(catId);
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testCreateCat_shouldCreateANewCat() {

        // Arrange
        Cat expectedCat = CatFactory.randomCat().build();
        Customer customer = CustomerFactory.randomCustomer().build();

        when(customerService.getCustomer(customer.getUsername())).thenReturn(customer);
        when(catRepository.save(expectedCat)).thenReturn(expectedCat);

        // Act
        Cat resultCat = catService.createCat(expectedCat, customer.getUsername());

        // Assert
        assertEquals(expectedCat, resultCat);
        verify(customerService, times(1)).getCustomer(customer.getUsername());
        verify(catRepository, times(1)).save(expectedCat);
    }

    @Test
    void testCreateCat_ownerUnknown_shouldThrowUsernameNotFoundException() {

        // Arrange
        Cat expectedCat = CatFactory.randomCat().build();
        String unknownOwnername = "unknownOwnername";
        when(customerService.getCustomer(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> catService.createCat(expectedCat, unknownOwnername));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testEditCat_shouldEditExistingCat() {

        // Arrange
        Cat cat = CatFactory.randomCat().build();

        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        when(customerService.getCustomer(cat.getOwner().getUsername())).thenReturn(cat.getOwner());
        when(catRepository.save(cat)).thenReturn(cat);

        // Act
        Cat resultCat = catService.editCat(cat.getId(), cat, cat.getOwner().getUsername());

        // Assert
        assertEquals(cat, resultCat);
        verify(catRepository, times(1)).findById(cat.getId());
        verify(customerService, times(1)).getCustomer(cat.getOwner().getUsername());
        verify(catRepository, times(1)).save(cat);
    }

    @Test
    void testEditCat_nonExistingCat_shouldThrowRecordNotFoundException() {

        // Arrange
        UUID catId = UUID.randomUUID();
        Cat cat = CatFactory.randomCat().build();
        String ownerUsername = "randomOwnerName";
        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.editCat(catId, cat, ownerUsername));

        // Assert
        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository).findById(catId);
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testDeleteCat_ShouldDeleteCatWithSpecificId() {

        // Arrange
        Cat cat = CatFactory.randomCat().build();
        when(catRepository.existsById(cat.getId())).thenReturn(true);

        // Act
        UUID catId = catService.deleteCat(cat.getId());

        // Assert
        verify(catRepository, times(1)).existsById(catId);
        verify(catRepository, times(1)).deleteById(catId);
        verifyNoInteractions(customerService);
    }

    @Test
    void testDeleteCat_shouldDeleteCatWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID catId = UUID.randomUUID();
        when(catRepository.existsById(catId)).thenReturn(false);

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catService.deleteCat(catId));

        // Assert
        assertEquals("No cat found with this id.", exception.getMessage());
        verify(catRepository, never()).deleteById(catId);
        verifyNoMoreInteractions(catRepository);
        verifyNoInteractions(customerService);
    }
}

