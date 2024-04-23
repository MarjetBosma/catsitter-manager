package nl.novi.catsittermanager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import nl.novi.catsittermanager.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Slf4j
public class User implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;
    private Role role;
    @Column(nullable = false)
    private Boolean enabled;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String email;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void enable() {
        this.enabled = true;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        log.error("ROLE: " + getRole());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + getRole()));

        return authorities;
    }
}
