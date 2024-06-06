package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record OrderRequest(
        @NotNull(message = "Start date is required")
        String startDate,
        @NotNull(message = "End date is required")
        String endDate,
        @NotNull(message = "Daily number of visits is required")
        @Positive
        int dailyNumberOfVisits,
        @NotNull(message = "Total number of visits is required")
        @Positive
        int totalNumberOfVisits,
        @NotNull(message = "Give the username of the customer to which this order belongs")
        String customerUsername,
        @NotNull(message = "Give the username of the catsitter to which this order belongs")
        String catsitterUsername
) {
}

