package nl.novi.catsittermanager.dtos.user;

import jakarta.persistence.GeneratedValue;
import nl.novi.catsittermanager.enumerations.Role;
//import nl.novi.catsittermanager.models.Authority;

import java.util.Set;
import java.util.UUID;

public record UserDto (

    @GeneratedValue
    UUID id,
    String username,
    String password,
    Role role,
//    Set<Authority> authorities,
    String authorities, // Dummy
    Boolean enabled,
    String name,
    String address,
    String email

) {
}