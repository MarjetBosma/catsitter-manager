package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderDto(

        UUID orderNo,
        LocalDate startDate,
        LocalDate endDate,
        int dailyNumberOfVisits,
        int totalNumberOfVisits,

        List<Task> task,

        Customer customer,

        Catsitter catsitter,

        Invoice invoice

) {
}
