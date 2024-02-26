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

//public static UserDto fromUser(User user){
//
//    var dto = new UserDto();
//
//    dto.username = user.getUsername();
//    dto.password = user.getPassword();
//    dto.role = user.getRole();
//    dto.enabled = user.isEnabled();
//    dto.email = user.getEmail();
//    dto.authorities = user.getAuthorities();
//
//    return dto;
//}
//
//public User toUser(UserDto userDto) {
//
//    var user = new User();
//
//    user.setUsername(userDto.username);
//    user.setPassword(userDto.password);
//    user.setRole(userDto.role);
//    user.setEnabled(userDto.enabled);
//    user.setEmail(userDto.email);
//
//    return user;
}