//package nl.novi.catsittermanager.services;
//
//import nl.novi.catsittermanager.dtos.user.UserDto;
//import nl.novi.catsittermanager.models.Authority;
//
//import nl.novi.catsittermanager.models.User;
//import nl.novi.catsittermanager.repositories.UserRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserServiceImplementation userService;
//
//    private final UserRepository userRepos;
//
//    public CustomUserDetailsService(UserServiceImplementation userService, UserRepository userRepos) {
//        this.userService = userService;
//        this.userRepos = userRepos;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//
//        UserDto userDto = userService.getUser(username);
//
//        Optional<User> user = userRepos.findById(username);
//
//        String password = user.get().getPassword();
//
//        Set<Authority> authorities = userDto.authorities();
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority: authorities) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
//        }
//
//        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
//    }
//
//}
