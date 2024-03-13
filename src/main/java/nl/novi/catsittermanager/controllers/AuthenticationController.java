//package nl.novi.catsittermanager.controllers;
//
//import nl.novi.catsittermanager.payload.AuthenticationRequest;
//import nl.novi.catsittermanager.payload.AuthenticationResponse;
//import nl.novi.catsittermanager.services.CustomUserDetailsService;
//import nl.novi.catsittermanager.utils.JwtUtil;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@CrossOrigin
//@RestController
//public class AuthenticationController {
//
//    private final AuthenticationManager authenticationManager;
//
//    private final CustomUserDetailsService userDetailsService;
//
//    private final JwtUtil jwtUtil;
//
//    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
//        this.authenticationManager = authenticationManager;
//        this.userDetailsService = userDetailsService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    /*
//         Deze methode geeft de principal (basis user gegevens) terug van de ingelogde gebruiker
//     */
//    @GetMapping(value = "/authenticated")
//    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
//        return ResponseEntity.ok().body(principal);
//    }
//
//    /*
//    Deze methode geeft het JWT token terug wanneer de gebruiker de juiste inloggegevens opgeeft.
//     */
//    @PostMapping(value = "/authenticate")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//
//        String email = authenticationRequest.getEmail();
//        String password = authenticationRequest.getPassword();
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(email, password)
//            );
//        }
//        catch (BadCredentialsException ex) {
//            throw new Exception("Incorrect username or password", ex);
//        }
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(email);
//
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }
//
//}