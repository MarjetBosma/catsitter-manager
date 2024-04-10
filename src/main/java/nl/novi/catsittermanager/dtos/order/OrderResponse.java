package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.dtos.task.TaskResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID orderNo,
        LocalDate startDate,
        LocalDate endDate,
        int dailyNumberOVisits,
        int totalNumberOfVisits,
        List<TaskResponse> tasks

        // invoice

        // customer Id, cat id, catsitter id
) {
}
