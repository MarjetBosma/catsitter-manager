package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.models.UserFactory;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService  userService;

    @Test
    void testGetAllUsers_shouldFetchAllUsersOnTheList() {
        // Given
        User expectedUser = UserFactory.randomUser().build();
        List<User> expectedUserList = List.of(expectedUser);

        when(userRepository.findAll()).thenReturn(expectedUserList);

        // When
        List<User> userResponseList = userService.getAllUsers();

        // Then
        assertEquals(expectedUserList, userResponseList);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_noUserInDatabase_shouldReturnEmptyList() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> userResponseList = userService.getAllUsers();

        // Then
        assertTrue(userResponseList.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUser_shouldFetchUserWithSpecificUsername() {
        // Given
        User expectedUser = UserFactory.randomUser().build();

        when(userRepository.findById(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));

        // When
        User resultUser = userService.getUser(expectedUser.getUsername());

        // Then
        assertEquals(expectedUser, resultUser);
        verify(userRepository, times(1)).findById(expectedUser.getUsername());
    }

    @Test
    void testGetUser_shouldFetchUserWithSpecificUsername_RecordNotFoundException() {
        // Given
        String username = "nonExistingUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception=assertThrows(RecordNotFoundException.class, () -> userService.getUser(username));

        // Then
        assertEquals("No user found with this username.", exception.getMessage());
        verify(userRepository).findById(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testCreateAdminAccount_shouldCreateANewAdminAccount() {
        // Given
        User expectedAdminAccount = UserFactory.randomUser().build();

        when(userRepository.save(expectedAdminAccount)).thenReturn(expectedAdminAccount);

        // When
        User resultAdminAccount = userService.createAdminAccount(expectedAdminAccount);

        // Then
        assertEquals(expectedAdminAccount, resultAdminAccount);

        verify(userRepository, times(1)).save(expectedAdminAccount);
    }

    @Test
    void testCreateNewAdminAccount_WhenUsernameExists_shouldThrowUsernameAlreadyExistsException() {
        // Given
        User existingUser = UserFactory.randomUser().build();
        String existingUsername = "existingUsername";
        existingUser.setUsername(existingUsername);

        when(userRepository.findById(existingUsername)).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.createAdminAccount(existingUser);
        });
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testEditUser_shouldEditExistingUser() {
        // Given
        User user = UserFactory.randomUser().build();

        when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User resultUser = userService.editUser(user.getUsername(), user);

        // Then
        assertEquals(user, resultUser);

        verify(userRepository, times(1)).findById(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testEditUser_nonExistingUser_shouldThrowRecordNotFoundException() {
        // Given
        String username = "nonExistingUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> userService.getUser(username));

        // When & Then
        assertEquals("No user found with this username.", exception.getMessage());
        verify(userRepository, times(1)).findById(username);
    }

    @Test
    void testDeleteUser_shouldDeleteUserWithSpecificId() {
        // Given
        User user = UserFactory.randomUser().build();
        when(userRepository.existsById(user.getUsername())).thenReturn(true);

        // When
        String resultUsername = userService.deleteUser(user.getUsername());

        // Then
        assertEquals(user.getUsername(), resultUsername);

        verify(userRepository, times(1)).existsById(user.getUsername());
        verify(userRepository, times(1)).deleteById(user.getUsername());
    }

    @Test
    void testDeleteCustomer_nonExistingCustomer_shouldThrowRecordNotFoundException() {
        // Given
        String username = "nonExistingUser";
        when(userRepository.existsById(username)).thenReturn(false);

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(username));
        verify(userRepository).existsById(username);
        verifyNoMoreInteractions(userRepository);
    }

}
