package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
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
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Slf4j
public class User implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column
    private Role role;

    @Column
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

    public User(String username, String password, Role role, Boolean enabled, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public <T> User(String username, String password, List<T> ts) {
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
