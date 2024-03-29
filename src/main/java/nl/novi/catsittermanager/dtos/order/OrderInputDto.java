package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import nl.novi.catsittermanager.models.*;

import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public record OrderInputDto (

    @Future
    LocalDate startDate,
    @Future
    LocalDate endDate,
    @Positive
    int dailyNumberOfVisits,
    @Positive
    int totalNumberOfVisits,
    List<Task> task,
    Customer customer,
    Catsitter catsitter,
    Invoice invoice

) {
}

