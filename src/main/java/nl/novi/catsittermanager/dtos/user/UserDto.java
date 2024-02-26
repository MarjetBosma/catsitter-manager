package nl.novi.catsittermanager.dtos.user;

import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Authority;

import java.util.Set;

public record UserDto (
//    @GeneratedValue
    Long id,
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

) {}