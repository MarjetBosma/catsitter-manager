package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.models.User;

public class UserMapper {

    public static UserDto transferToDto(User user) {
        return new UserDto(user.getId(),
                           user.getUsername(),
                           user.getPassword(),
                           user.getRole(),
                           user.getAuthorities(),
                           user.getEnabled(),
                           user.getName(),
                           user.getAddress(),
                           user.getEmail()
        );
    }

    public static User transferFromDto(UserInputDto userInputDto) {
        return new User(userInputDto.id(), // In een latere fase deze hier niet meegeven, maar automatisch via database
                        userInputDto.username(),
                        userInputDto.password(),
                        userInputDto.role(),
                        userInputDto.authorities(),
                        userInputDto.enabled(),
                        userInputDto.name(),
                        userInputDto.address(),
                        userInputDto.email()
        );
    }
}