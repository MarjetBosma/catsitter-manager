package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    public Long orderNo;
    public LocalDate startDate;
    public LocalDate endDate;
    public int dailyNumberOfVisits;
    public int totalNumberOfVisits;
    public List<Task> taskList = new ArrayList<>();
    public Customer customer;
    public CatSitter catSitter;
    public Invoice invoice;
}
