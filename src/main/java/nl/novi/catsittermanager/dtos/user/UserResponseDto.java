package nl.novi.catsittermanager.dtos.user;
import nl.novi.catsittermanager.models.Authority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;

public class UserResponseDto {
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;

    public UserResponseDto(String email, Set<Authority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }
}

