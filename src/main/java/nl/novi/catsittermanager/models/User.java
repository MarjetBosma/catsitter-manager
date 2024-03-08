package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;
import nl.novi.catsittermanager.enumerations.Role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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

    String authorities; // Dummy

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
