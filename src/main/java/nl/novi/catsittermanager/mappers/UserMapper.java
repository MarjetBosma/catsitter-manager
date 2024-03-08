package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.user.UserDto;
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

//    public static User transferFromDto(UserInputDto userInputDto) {
//        User.builder().id(userInputDto.id())
//                .username(userInputDto.username())
//                .password(userInputDto.password())
//                .role(userInputDto.role())
//                .address(userInputDto.address())
//                .email(userInputDto.email())
//                .build();
//    }
}