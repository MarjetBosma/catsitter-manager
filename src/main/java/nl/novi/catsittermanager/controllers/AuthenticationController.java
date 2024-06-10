package nl.novi.catsittermanager.controllers;

import lombok.AllArgsConstructor;
import nl.novi.catsittermanager.dtos.login.ErrorResponse;
import nl.novi.catsittermanager.dtos.login.LoginRequest;
import nl.novi.catsittermanager.dtos.login.LoginResponse;
import nl.novi.catsittermanager.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String token = jwtUtil.createToken(userDetails.getUsername());
                LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), token);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginResponse);
            } else {
                throw new AuthenticationServiceException("Unexpected authentication principal: " + principal);
            }

        } catch (BadCredentialsException badCredentialsException) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }
}