package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.models.*;

import java.util.ArrayList;
import java.util.List;

public class OrderInputDto {
    public List<Task> taskList = new ArrayList<>();
    @NotNull(message = "customer is required")
    public Customer customer;
    @NotNull(message = "catsitter is required")
    public CatSitter catSitter;
    public Invoice invoice;
    public List<Order> orderList = new ArrayList<>();
}
