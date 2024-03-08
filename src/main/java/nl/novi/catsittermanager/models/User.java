package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.catsittermanager.enumerations.Role;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    String authorities; // Dummy, alleen voor testen zonder database
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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
//    private Set<Authority> authorities = new HashSet<>();
    private Role role;
    //    @Column(nullable = false)
    private Boolean enabled;

    //    @Column
    private String name;

    //    @Column
    private String address;

    //    @Column
    private String email;


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
