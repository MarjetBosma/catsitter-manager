package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.List;

public class CatSitterInputDto {
    public String about;
    public List<Order> orderList = new ArrayList<>();
    public List<Customer> customers = new ArrayList<>();
}
