package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.models.*;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    public Long orderNo;
    public List<Task> taskList = new ArrayList<>();
    public Customer customer;
    public CatSitter catSitter;
    public Invoice invoice;
    public List<Order> orderList = new ArrayList<>();
}
