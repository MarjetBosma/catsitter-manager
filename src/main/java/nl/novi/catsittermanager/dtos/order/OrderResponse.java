package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.dtos.task.TaskResponse;
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
        List<TaskResponse> tasks,
        Customer customer,
        Catsitter catsitter,
        UUID invoiceNo

) {
}
