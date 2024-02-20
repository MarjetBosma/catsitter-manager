package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.models.User;

public class UserMapper {
    public static UserDto fromUser(User user){

        var dto = new UserDto();

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.email = user.getEmail();
        dto.authorities = user.getAuthorities();

        return dto;
    }

    public User toUser(UserDto userDto) {

        var user = new User();

        user.setUsername(userDto.username);
        user.setPassword(userDto.password);
        user.setEnabled(userDto.enabled);
        user.setEmail(userDto.email);

        return user;
    }
}
