package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.models.User;

public class UserMapper {

    public static UserResponse UserToUserResponse(User user) {

        return new UserResponse(
                user.getUsername(),
                user.getRole(),
                user.getEnabled(),
                user.getName(),
                user.getAddress(),
                user.getEmail()
        );
    }

    public static User UserRequestToUser(UserRequest userRequest) {

        return User.builder()
                .username(userRequest.username())
                .password(userRequest.password())
                .role(userRequest.role())
                .enabled(userRequest.enabled())
                .name(userRequest.name())
                .address(userRequest.address())
                .email(userRequest.email())
                .build();
    }
}