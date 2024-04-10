package nl.novi.catsittermanager.dtos.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Authority;

import java.util.Set;

public record UserResponse(
        String username,
        String password,
        Role role,
        @JsonSerialize
        Set<Authority> authorities,
        Boolean enabled,
        String name,
        String address,
        String email

) {
}

