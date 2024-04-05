package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //onderstaande willen we halen uit de database op basis van rol.
        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User with given username is not found: " + username));

        //todo member.getRoles en daar de hieronder roles aan toe te voegen.

//        //todo hij maakt dus een userDetails aan met oa roles en voegt 'ie een member toe, ik wil dat al bijhouden in lijst authorities, moet ik het dan nog een keer doen?
//        //todo wat wordt er met deze roles gedaan?

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        //hier zouden we ze uit de database moeten halen.
                        //onderstaande zorgt ervoor hoe het in de database komt te staan, en dat is dus iets anders dan hoe het in de authentication te vinden is.
                        //.roles(user.getRoles())
                        .roles(new String[]{"USER"})
                        .build();
        return userDetails;
    }

}
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        UserDto userDto = userService.getUser(username);
//
//        List<String> roles = new ArrayList<>(); // is dit nodig als ik al een enum heb met de rollen?
//        roles.add("USER"); // Rollen in de enum Roles zijn CUSTOMER, CATSITTER en ADMIN
//
//        UserDetails userDetails =
//                org.springframework.security.core.userdetails.User.builder()
//                        .username(userDto.username())
//                        .password(userDto.password())
//                        .roles(roles.toArray(new String[0]))
//                        .build();
//        return userDetails;
//
//    }

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


