package nl.novi.catsittermanager.dtos.user;

import jakarta.persistence.GeneratedValue;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Authority;
//import nl.novi.catsittermanager.models.Authority;

import java.util.Set;
import java.util.UUID;

public record UserDto (

    String username,
    String password,
    String email,
    Role role,
    Set<Authority> authorities,
    Boolean enabled,
    String name,
    String address

) {
}