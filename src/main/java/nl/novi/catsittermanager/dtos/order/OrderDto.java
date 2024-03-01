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

//    List<Task> taskList,
    String taskList, // Alleen voor los testen Order class zonder database

//    Customer customer,
    String customer,  // Alleen voor los testen Order class zonder database

//    CatSitter catSitter,
    String catSitter, // Alleen voor los testen Order class zonder database

    Invoice invoice

) {}
