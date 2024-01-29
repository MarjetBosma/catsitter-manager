package nl.novi.catsittermanager.dtos.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.catsittermanager.models.Authority;

import jakarta.validation.constraints.*;

import java.util.Set;

public class UserInputDto {

    @NotNull(message = "username is required")
    public String username;

    @NotNull(message = "password is required")
    public String password;
    public Boolean enabled;

    public String name;
    public String address;
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;
}
