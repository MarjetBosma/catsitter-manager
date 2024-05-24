package nl.novi.catsittermanager.dtos.order;

import nl.novi.catsittermanager.dtos.task.TaskResponse;

import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID orderNo,
        String startDate,
        String endDate,
        int dailyNumberOfVisits,
        int totalNumberOfVisits,
        List<TaskResponse> tasks

) {
}
