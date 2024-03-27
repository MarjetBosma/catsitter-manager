package nl.novi.catsittermanager.models.response;

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

//public class AuthenticationResponse {
//
//    private final String jwt;
//
//    public AuthenticationResponse(String jwt) {
//        this.jwt = jwt;
//    }
//
//    public String getJwt() {
//        return jwt;
//    }
//
//}
