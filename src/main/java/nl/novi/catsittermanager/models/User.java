package nl.novi.catsittermanager.models;


import lombok.Getter;
import lombok.Setter;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.services.UserServiceImplementation;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

//@Entity
//@Table(name = "users")
public class User {

//    @Id
    private Long id;
//    @Column(nullable = false, unique = true)
    private String username;
    //    @Column(nullable = false, length = 255)
    private String password;

    private Role role;

//    @OneToMany(
//            targetEntity = Authority.class,
//            mappedBy = "username",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.EAGER)
//    private Set<Authority> authorities = new HashSet<>();

    String authorities; // Dummy, alleen voor testen zonder database

    //    @Column(nullable = false)
    private Boolean enabled;

    //    @Column
    private String name;

    //    @Column
    private String address;

    //    @Column
    private String email;

    public User() {
    }

    public User(Long id, String username, String password, Role role, String authorities, boolean enabled, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorities = authorities;  // Bij gebruik database datatype terugzetten naar Set
        this.enabled = enabled;
        this.name = name;
        this.address = address;
        this.email = email;
    }

//    public Set<Authority> getAuthorities() {
//        return authorities;
//    }
//
//    public void addAuthority(Authority authority) {
//        this.authorities.add(authority);
//    }
//
//    public void removeAuthority(Authority authority) {
//        this.authorities.remove(authority);
//    }

}
