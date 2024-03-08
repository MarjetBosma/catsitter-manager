package nl.novi.catsittermanager.dtos.user;

import nl.novi.catsittermanager.enumerations.Role;

import java.util.UUID;

public record UserDto(
//    @GeneratedValue
        UUID id,
        String username,
        String password,
        Role role,

//    Set<Authority> authorities,
        String authorities,
        // Dummy, alleen voor los testen Cat class zonder database
        Boolean enabled,
        String name,
        String address,
        String email

) {
}