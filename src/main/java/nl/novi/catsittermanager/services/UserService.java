package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(long idToFind);

    UserDto createUser(UserInputDto userInputDto);

    UserDto editUser(long IdToEdit, UserInputDto userInputDto);

    long deleteUser(long IdToDelete);

}
