package nl.novi.catsittermanager.models;

import nl.novi.catsittermanager.enumerations.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    void testConstructorWithUsernameAndPassword() {
        // Arrange
        String username = "testuser";
        String password = "password";

        // Act
        User user = new User(username, password);

        // Assert
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(password, user.getPassword());
    }

    @Test
    void testEnable() {
        // Arrange
        user.setEnabled(false);

        // Act
        user.enable();

        // Assert
        Assertions.assertTrue(user.getEnabled());
    }

    @Test
    void testGetAuthorities() {
        // Arrange
        user.setRole(Role.CUSTOMER);

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        Assertions.assertEquals(1, authorities.size());
        Assertions.assertTrue(((Collection<?>) authorities).contains(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }
}
