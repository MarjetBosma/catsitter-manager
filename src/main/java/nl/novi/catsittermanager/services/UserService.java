package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(UUID idToFind);

    UserDto createUser(UserInputDto userInputDto);

    UserDto editUser(UUID IdToEdit, UserInputDto userInputDto);

    UUID deleteUser(UUID IdToDelete);
}
