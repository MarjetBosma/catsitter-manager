package nl.novi.catsittermanager.controllers;

import lombok.AllArgsConstructor;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.models.request.LoginRequest;
import nl.novi.catsittermanager.models.response.LoginResponse;
import nl.novi.catsittermanager.models.response.ErrorResponse;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    // Geeft foutmelding en wil niet starten: Parameter 0 of constructor in nl.novi.catsittermanager.controllers.AuthenticationController required a bean of type 'org.springframework.security.authentication.AuthenticationManager' that could not be found.

    private JwtUtil jwtUtil;

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginRequest loginRequest)  {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            String username = authentication.getName();
            User user = new User(username, ""); // Later anders doen, met gebruik van password encoder
            String token = jwtUtil.createToken(user);
            LoginResponse loginResponse = new LoginResponse(username,token);

            return ResponseEntity.ok(loginResponse);

        }catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


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
//    Deze methode geeft het JWT token terug wanneer de gebruiker de juiste inloggegevens op geeft.
//     */
//    @PostMapping(value = "/authenticate")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//
//        String username = authenticationRequest.getUsername();
//        String password = authenticationRequest.getPassword();
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//        }
//        catch (BadCredentialsException ex) {
//            throw new Exception("Incorrect username or password", ex);
//        }
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(username);
//
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }
//
}