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

        // Arrange
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();
        List<Catsitter> expectedCatsitterList = List.of(expectedCatsitter);

        when(catsitterRepository.findAll()).thenReturn(expectedCatsitterList);

        // Act
        List<Catsitter> catsitterResponseList = catsitterService.getAllCatsitters();

        // Assert
        assertEquals(expectedCatsitterList, catsitterResponseList);
        verify(catsitterRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCatsitters_noCatsittersInDatabase_shouldReturnEmptyList() {

        // Arrange
        when(catsitterRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Catsitter> catsitterResponseList = catsitterService.getAllCatsitters();

        // Assert
        assertTrue(catsitterResponseList.isEmpty());
        verify(catsitterRepository, times(1)).findAll();
    }

    @Test
    void testGetCatsitter_shouldFetchCatsitterWithSpecificUsername() {

        // Arrange
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.findById(expectedCatsitter.getUsername())).thenReturn(Optional.of(expectedCatsitter));

        // Act
        Catsitter resultCatsitter = catsitterService.getCatsitter(expectedCatsitter.getUsername());

        // Assert
        assertEquals(expectedCatsitter, resultCatsitter);
        verify(catsitterRepository, times(1)).findById(expectedCatsitter.getUsername());
    }

    @Test
    void testGetCatsitter_shouldFetchCatsitterWithSpecificUsername_RecordNotFoundException() {

        // Arrange
        String username = "nonExistingCatsitter";

        when(catsitterRepository.findById(username)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception=assertThrows(RecordNotFoundException.class, () -> catsitterService.getCatsitter(username));

        // Assert
        assertEquals("No catsitter found with this username.", exception.getMessage());
        verify(catsitterRepository, times(1)).findById(username);
    }

    @Test
    void testGetAllOrdersByCatsitter_shouldFetchAllOrdersForThisCatsitter() {

        // Arrange
        Catsitter randomCatsitter = CatsitterFactory.randomCatsitter().build();
        List<Order> expectedOrders = OrderFactory.randomOrders(3);
        randomCatsitter.setOrders(expectedOrders);

        when(catsitterRepository.findById(randomCatsitter.getUsername())).thenReturn(Optional.of(randomCatsitter));

        // Act
        List<Order> resultOrders = catsitterService.getAllOrdersByCatsitter(randomCatsitter.getUsername());

        // Assert
        assertEquals(expectedOrders.size(), resultOrders.size());
        assertTrue(resultOrders.containsAll(expectedOrders));
        verify(catsitterRepository, times(1)).findById(randomCatsitter.getUsername());
    }

    @Test
    void testGetAllOrdersByCatsitter_noOrdersOnTheList_shouldReturnEmptyList() {

        // Arrange
        Catsitter randomCatsitter = CatsitterFactory.randomCatsitter().build();
        randomCatsitter.setOrders(Collections.emptyList());

        when(catsitterRepository.findById(randomCatsitter.getUsername())).thenReturn(Optional.of(randomCatsitter));

        // Act
        List<Order> resultOrders = catsitterService.getAllOrdersByCatsitter(randomCatsitter.getUsername());

        // Assert
        assertNotNull(resultOrders);
        assertTrue(resultOrders.isEmpty());
        verify(catsitterRepository, times(1)).findById(randomCatsitter.getUsername());
    }

    @Test
    void testCreateCatsitter_shouldCreateANewCatsitter() {

        // Arrange
        Catsitter expectedCatsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.save(expectedCatsitter)).thenReturn(expectedCatsitter);

        // Act
        Catsitter resultCatsitter = catsitterService.createCatsitter(expectedCatsitter);

        // Assert
        assertEquals(expectedCatsitter, resultCatsitter);
        verify(catsitterRepository, times(1)).save(expectedCatsitter);
    }

    @Test
    void testCreateCatsitter_WhenUsernameExists_shouldThrowUsernameAlreadyExistsException() {

        // Arrange
        Catsitter existingCatsitter = CatsitterFactory.randomCatsitter().build();
        String existingUsername = "existingUsername";
        existingCatsitter.setUsername(existingUsername);

        when(catsitterRepository.findById(existingUsername)).thenReturn(Optional.of(existingCatsitter));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            catsitterService.createCatsitter(existingCatsitter);
        });
        verifyNoMoreInteractions(catsitterRepository);
    }

    @Test
    void testEditCatsitter_shouldEditExistingCatsitter() {

        // Arrange
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();

        when(catsitterRepository.findById(catsitter.getUsername())).thenReturn(Optional.of(catsitter));
        when(catsitterRepository.save(catsitter)).thenReturn(catsitter);

        // Act
        Catsitter resultCatsitter = catsitterService.editCatsitter(catsitter.getUsername(), catsitter);

        // Assert
        assertEquals(catsitter, resultCatsitter);
        verify(catsitterRepository, times(1)).findById(catsitter.getUsername());
        verify(catsitterRepository, times(1)).save(catsitter);
    }

    @Test
    void testEditCatsitter_nonExistingCatsitter_shouldThrowRecordNotFoundException() {

        // Arrange
        String username = "nonExistingCatsitter";
        when(catsitterRepository.findById(username)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> catsitterService.getCatsitter(username));

        // Act & Assert
        assertEquals("No catsitter found with this username.", exception.getMessage());
        verify(catsitterRepository, times(1)).findById(username);
    }

    @Test
    void testDeleteCatsitter_shouldDeleteCatsitterWithSpecificId() {

        // Arrange
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();
        when(catsitterRepository.existsById(catsitter.getUsername())).thenReturn(true);

        // Act
        String resultUsername = catsitterService.deleteCatsitter(catsitter.getUsername());

        // Assert
        assertEquals(catsitter.getUsername(), resultUsername);
        verify(catsitterRepository, times(1)).existsById(catsitter.getUsername());
        verify(catsitterRepository, times(1)).deleteById(catsitter.getUsername());
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
