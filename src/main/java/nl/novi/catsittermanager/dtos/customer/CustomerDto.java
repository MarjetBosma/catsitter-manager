package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatSitter;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDto {

    public Long id;
    public int numberOfCats;
    public List<Order> orderList = new ArrayList<>();
    public Set<Cat> catListByName = new HashSet<>();
    public List<CatSitter> catsitters = new ArrayList<>();
}