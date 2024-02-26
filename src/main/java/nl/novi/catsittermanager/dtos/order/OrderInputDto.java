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

    Long orderNo, //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
    @Future
    LocalDate startDate,
    @Future
    LocalDate endDate,
    @Positive
    int dailyNumberOfVisits,
    @Positive
    int totalNumberOfVisits,

    //    List<Task> taskList,
    String taskList, // Alleen voor los testen Order class zonder database

    //    Customer customer,
    String customer,  // Alleen voor los testen Order class zonder database

    //    CatSitter catSitter,
    String catSitter, // Alleen voor los testen Order class zonder database

    //    Invoice invoice
    String invoice // Alleen voor los testen Order class zonder database

) {}

//    @Future
//    public LocalDate startDate;
//    @Future
//    public LocalDate endDate;
//    @Positive
//    public int dailyNumberOfVisits;
//    @Positive
//    public int totalNumberOfVisits;
//    public List<Task> taskList = new ArrayList<>();
//    @NotNull(message = "customer is required")
//    public Customer customer;
//    @NotNull(message = "catsitter is required")
//    public CatSitter catSitter;
//    public Invoice invoice;

