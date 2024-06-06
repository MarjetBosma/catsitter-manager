package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_userExists() {

        // Arrange
        String username = "testuser";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.ADMIN)
                .build();
        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Set<GrantedAuthority> expectedAuthorities = Collections.singleton(expectedAuthority);

        Set<GrantedAuthority> actualAuthorities = userDetails.getAuthorities().stream().collect(Collectors.toSet());
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(expectedAuthorities, actualAuthorities);

        verify(userRepository, times(1)).findById(username);
    }

    @Test
    void testLoadUserByUsername_userDoesNotExist() {

        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
        assertEquals("User with given username is not found: " + username, exception.getMessage());

        verify(userRepository, times(1)).findById(username);
    }
}

