package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.models.Authority;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(String username);

    UserDto createUser(UserInputDto userInputDto);

    UserDto editUser(String username, UserInputDto userInputDto);

    String deleteUser(String username);

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

}
