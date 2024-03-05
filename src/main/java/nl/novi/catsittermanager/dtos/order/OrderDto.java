package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record OrderDto (

    Long orderNo,
    LocalDate startDate,
    LocalDate endDate,
    int dailyNumberOfVisits,
    int totalNumberOfVisits,

    Task task,

    Customer customer,

    Catsitter catsitter,

    Invoice invoice

) {}
