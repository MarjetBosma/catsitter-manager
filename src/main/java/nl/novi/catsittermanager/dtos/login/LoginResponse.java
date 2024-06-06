package nl.novi.catsittermanager.dtos.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String username;
    private String token;
}

