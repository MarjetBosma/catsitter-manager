package nl.novi.catsittermanager.dtos.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "username is required")
    @NotBlank(message = "username is required")
    private String username;
    @NotNull(message = "password is required")
    @NotBlank(message = "username is required")
    private String password;
}
