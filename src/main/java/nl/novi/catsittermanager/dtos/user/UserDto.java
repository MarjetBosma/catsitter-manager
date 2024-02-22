package nl.novi.catsittermanager.dtos.user;

import nl.novi.catsittermanager.models.Authority;

import java.util.Set;

public class UserDto {

//    @GeneratedValue
    public Long id;

    public String username;
    public String password;
    public Boolean enabled;

    public String name;
    public String address;
    public String email;
    public Set<Authority> authorities;

   }