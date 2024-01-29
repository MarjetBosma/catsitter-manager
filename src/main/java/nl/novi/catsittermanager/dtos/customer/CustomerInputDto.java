package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerInputDto {
    public int numberOfCats;
    public List<Order> orderList = new ArrayList<>();
    public Set<Cat> catListByName = new HashSet<>();
}
