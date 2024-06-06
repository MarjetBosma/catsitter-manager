package nl.novi.catsittermanager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class PasswordEncodeServiceTest {
    private PasswordEncoderService passwordEncoderService;
    private BCryptPasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
        passwordEncoderService = new PasswordEncoderService();

        Field passwordEncoderField = PasswordEncoderService.class.getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);
        passwordEncoderField.set(passwordEncoderService, mockPasswordEncoder);
    }

    @Test
    void testHashPassword() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = "$2a$10$WzNfRUO.jcs/RHxjBP5IluZ5OE8XZg4cJfDPQ6Lk.ygYz6qA2sNEO";
        when(mockPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
        String result = passwordEncoderService.hashPassword(rawPassword);

        // Assert
        Assertions.assertEquals(encodedPassword, result);
        verify(mockPasswordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void testVerifyPassword() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = "$2a$10$WzNfRUO.jcs/RHxjBP5IluZ5OE8XZg4cJfDPQ6Lk.ygYz6qA2sNEO";
        when(mockPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        boolean result = passwordEncoderService.verifyPassword(rawPassword, encodedPassword);

        // Assert
        Assertions.assertTrue(result);
        verify(mockPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void testVerifyPasswordFails() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = "$2a$10$WzNfRUO.jcs/RHxjBP5IluZ5OE8XZg4cJfDPQ6Lk.ygYz6qA2sNEO";
        when(mockPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        boolean result = passwordEncoderService.verifyPassword(rawPassword, encodedPassword);

        // Assert
        Assertions.assertFalse(result);
        verify(mockPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}
