package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record OrderDto (

    UUID orderNo,
    LocalDate startDate,
    LocalDate endDate,
    int dailyNumberOVisits,
    int totalNumberOfVisits,
    List<Task> task,
    Customer customer,
    Catsitter catsitter,
    Invoice invoice

) {
}
