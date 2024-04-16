package nl.novi.catsittermanager.dtos.user;

import nl.novi.catsittermanager.enumerations.Role;

public record UserResponse(
        String username,
        // String password,
        Role role,
        Boolean enabled,
        String name,
        String address,
        String email

) {
}

