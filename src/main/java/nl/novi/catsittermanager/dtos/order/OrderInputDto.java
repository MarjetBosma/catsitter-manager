package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderInputDto {

    @Future
    public LocalDate startDate;
    @Future
    public LocalDate endDate;
    @Positive
    public int dailyNumberOfVisits;
    @Positive
    public int totalNumberOfVisits;
    public List<Task> taskList = new ArrayList<>();
    @NotNull(message = "customer is required")
    public Customer customer;
    @NotNull(message = "catsitter is required")
    public CatSitter catSitter;
    public Invoice invoice;
}
