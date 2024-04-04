package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID orderNo,
        LocalDate startDate,
        LocalDate endDate,
        int dailyNumberOVisits,
        int totalNumberOfVisits,
        List<TaskDto> tasks,
        Customer customer,
        Catsitter catsitter,
        Invoice invoice

) {
}
