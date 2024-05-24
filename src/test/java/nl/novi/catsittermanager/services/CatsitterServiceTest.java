package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class CatsitterServiceTest {

    @Mock
    CatsitterRepository catsitterRepository;

    @InjectMocks
    CatsitterService catsitterService;

    @Test
    void testGetAllCatsitters_shouldFetchAllCatsittersOnTheList() {
        // Given
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();
        List<Catsitter> expectedCatsitterList = List.of(expectedCatsitter);

        when(catsitterRepository.findAll()).thenReturn(expectedCatsitterList);

        // When
        List<Catsitter> catsitterResponseList = catsitterService.getAllCatsitters();

        // Then
        assertEquals(expectedCatsitterList, catsitterResponseList);

        verify(catsitterRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCatsitters_noCatsittersInDatabase_shouldReturnEmptyList() {
        // Given
        when(catsitterRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Catsitter> catsitterResponseList = catsitterService.getAllCatsitters();

        // Then
        assertTrue(catsitterResponseList.isEmpty());
        verify(catsitterRepository, times(1)).findAll();
    }

    @Test
    void testGetCatsitter_shouldFetchCatsitterWithSpecificUsername() {
        // Given
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.findById(expectedCatsitter.getUsername())).thenReturn(Optional.of(expectedCatsitter));

        // When
        Catsitter resultCatsitter = catsitterService.getCatsitter(expectedCatsitter.getUsername());

        // Then
        assertEquals(expectedCatsitter, resultCatsitter);
        verify(catsitterRepository, times(1)).findById(expectedCatsitter.getUsername());
    }

    @Test
    void testGetCatsitter_shouldFetchCatsitterWithSpecificUsername_RecordNotFoundException() {
        // Given
        String username = "nonExistingCatsitter";

        when(catsitterRepository.findById(username)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception=assertThrows(RecordNotFoundException.class, () -> catsitterService.getCatsitter(username));

        // Then
        assertEquals("No catsitter found with this username.", exception.getMessage());
        verify(catsitterRepository).findById(username);
        verifyNoMoreInteractions(catsitterRepository);
    }

    @Test
    void testGetAllOrdersByCatsitter_shouldFetchAllOrdersForThisCatsitter() {
        // Given
        Catsitter randomCatsitter = CatsitterFactory.randomCatsitter().build();
        List<Order> expectedOrders = OrderFactory.randomOrders(3);
        randomCatsitter.setOrders(expectedOrders);

        when(catsitterRepository.findById(randomCatsitter.getUsername())).thenReturn(Optional.of(randomCatsitter));

        // When
        List<Order> resultOrders = catsitterService.getAllOrdersByCatsitter(randomCatsitter.getUsername());

        // Then
        assertEquals(expectedOrders.size(), resultOrders.size());
        assertTrue(resultOrders.containsAll(expectedOrders));

        verify(catsitterRepository, times(1)).findById(randomCatsitter.getUsername());
    }

    @Test
    void testGetAllOrdersByCatsitter_noOrdersOnTheList_shouldReturnEmptyList() {
        // Given
        Catsitter randomCatsitter = CatsitterFactory.randomCatsitter().build();
        randomCatsitter.setOrders(Collections.emptyList());

        when(catsitterRepository.findById(randomCatsitter.getUsername())).thenReturn(Optional.of(randomCatsitter));

        // When
        List<Order> resultOrders = catsitterService.getAllOrdersByCatsitter(randomCatsitter.getUsername());

        assertNotNull(resultOrders);
        assertTrue(resultOrders.isEmpty());

        verify(catsitterRepository, times(1)).findById(randomCatsitter.getUsername());
    }

    @Test
    void testCreateCatsitter_shouldCreateANewCatsitter() {
        // Given
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.save(expectedCatsitter)).thenReturn(expectedCatsitter);

        // When
        Catsitter resultCatsitter = catsitterService.createCatsitter(expectedCatsitter);

        // Then
        assertEquals(expectedCatsitter, resultCatsitter);

        verify(catsitterRepository, times(1)).save(expectedCatsitter);
    }

    @Test
    void testCreateCatsitter_WhenUsernameExists_shouldThrowUsernameAlreadyExistsException() {
        // Given
        Catsitter existingCatsitter = CatsitterFactory.randomCatsitter().build();
        String existingUsername = "existingUsername";
        existingCatsitter.setUsername(existingUsername);

        when(catsitterRepository.findById(existingUsername)).thenReturn(Optional.of(existingCatsitter));

        // When & Then
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            catsitterService.createCatsitter(existingCatsitter);
        });
        verifyNoMoreInteractions(catsitterRepository);
    }

    @Test
    void testEditCatsitter_shouldEditExistingCatsitter() {
        // Given
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.findById(catsitter.getUsername())).thenReturn(Optional.of(catsitter));
        when(catsitterRepository.save(catsitter)).thenReturn(catsitter);

        // When
        Catsitter resultCatsitter = catsitterService.editCatsitter(catsitter.getUsername(), catsitter);

        // Then
        assertEquals(catsitter, resultCatsitter);

        verify(catsitterRepository, times(1)).findById(catsitter.getUsername());
        verify(catsitterRepository, times(1)).save(catsitter);
    }

    @Test
    void testEditCatsitter_nonExistingCatsitter_shouldThrowRecordNotFoundException() {
        // Given
        String username = "nonExistingCatsitter";
        when(catsitterRepository.findById(username)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catsitterService.getCatsitter(username));

        // When & Then
        assertEquals("No catsitter found with this username.", exception.getMessage());
        verifyNoMoreInteractions(catsitterRepository);
    }

    @Test
    void testDeleteCatsitter_shouldDeleteCatsitterWithSpecificId() {
        // Given
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        when(catsitterRepository.existsById(catsitter.getUsername())).thenReturn(true);

        // When
        String resultUsername = catsitterService.deleteCatsitter(catsitter.getUsername());

        // Then
        assertEquals(catsitter.getUsername(), resultUsername);

        verify(catsitterRepository, times(1)).existsById(catsitter.getUsername());
        verify(catsitterRepository, times(1)).deleteById(catsitter.getUsername());
        verifyNoMoreInteractions(catsitterRepository);
    }

    @Test
    void testDeleteCatsitter_nonExistingCatsitter_shouldThrowRecordNotFoundException() {
        // Given
        String username = "nonExistingCatsitter";
        when(catsitterRepository.existsById(username)).thenReturn(false);

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> catsitterService.deleteCatsitter(username));
        verify(catsitterRepository).existsById(username);
        verifyNoMoreInteractions(catsitterRepository);
    }
}
