package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.models.Authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDto userDto = userService.getUser(username);

        List<String> roles = new ArrayList<>(); // is dit nodig als ik al een enum heb met de rollen?
        roles.add("USER"); // Rollen in de enum Roles zijn CUSTOMER, CATSITTER en ADMIN

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(userDto.username())
                        .password(userDto.password())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;

    }

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        UserDto userDto = userService.getUser(username);
//
//        String password = userDto.password();
//
//        Set<Authority> authorities = userDto.authorities();
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority: authorities) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
//        }
//
//        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
//    }

}
