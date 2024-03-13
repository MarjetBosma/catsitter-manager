package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.models.User;

public class UserMapper {

    public static UserDto transferToDto(User user) {
        return new UserDto(user.getUsername(),
                           user.getPassword(),
                           user.getEmail(),
                           user.getRole(),
                           user.getAuthorities(),
                           user.getEnabled(),
                           user.getName(),
                           user.getAddress()
        );
    }

    public static User transferFromDto(UserInputDto userInputDto) {
    return User.builder()
                .username(userInputDto.username())
                .password(userInputDto.password())
                .password(userInputDto.password())
                .role(userInputDto.role())
                .address(userInputDto.address())
                .build();
    }
}