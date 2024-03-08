package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public record OrderInputDto (

    Long orderNo,
    @Future
    LocalDate startDate,
    @Future
    LocalDate endDate,
    @Positive
    int dailyNumberOfVisits,
    @Positive
    int totalNumberOfVisits,

    Task task,

    Customer customer,

    Catsitter catsitter,

    Invoice invoice

) {}

