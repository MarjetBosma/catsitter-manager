package nl.novi.catsittermanager.dtos.user;

import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Authority;

import java.util.Set;

public record UserDto (
    String username,
    String password,
    Role role,
    Set<Authority> authorities,
    Boolean enabled,
    String name,
    String address,
    String email

) {
}