package nl.novi.catsittermanager.models;


import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

//@Entity
//@Table(name = "users")
public class User {


    // Deze eerste 3 variabelen zijn verplicht om te kunnen inloggen met een username, password en rol.
//    @Id
//    @Column(nullable = false, unique = true)
    private String username;

    //    @Column(nullable = false, length = 255)
    private String password;

    //    @OneToMany(
//            targetEntity = Authority.class,
//            mappedBy = "username",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    //    @Column(nullable = false)
    private boolean enabled = true;

    //    @Column
    private String name;

    //    @Column
    private String address;

    //    @Column
    private String email;

    // Role toevoegen vanuit enum, nog uitzoeken

    public User() {
    }

    public User(String username, String password, Set<Authority> authorities, boolean enabled, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

}
