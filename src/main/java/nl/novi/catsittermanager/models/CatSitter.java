package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "catsitters")
public class CatSitter extends User {

    private String about;

    @OneToMany(mappedBy = "catsitters")
    private List<Order> orderList = new ArrayList<>();

    public CatSitter() {}

    public CatSitter(String about, List<Order> orderList) {
        this.about = about;
        this.orderList = orderList;
    }
}
