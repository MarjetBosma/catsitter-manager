package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.UserRequestFactory;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.models.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    void testUserToUserMapper() {

        // Given
        User user = UserFactory.randomUser().build();

        // When
        UserResponse userResponse = UserMapper.UserToUserResponse(user);

        // Then
        assertEquals(user.getUsername(), userResponse.username());
        assertEquals(user.getName(), userResponse.name());
        assertEquals(user.getAddress(), userResponse.address());
        assertEquals(user.getEmail(), userResponse.email());
    }

    @Test
    void testUserRequestToUser() {

        // Given
        UserRequest userRequest = UserRequestFactory.randomUserRequest().build();

        // When
        User user = UserMapper.UserRequestToUser(userRequest);

        // Then
        assertEquals(userRequest.username(), user.getUsername());
        assertEquals(userRequest.name(), user.getName());
        assertEquals(userRequest.address(), user.getAddress());
        assertEquals(userRequest.email(), user.getEmail());
    }
}
