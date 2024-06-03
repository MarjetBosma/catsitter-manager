package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record OrderRequest(

        @NotNull
        String startDate,
        @NotNull
        String endDate,
        @NotNull
        @Positive
        int dailyNumberOfVisits,
        @NotNull
        @Positive
        int totalNumberOfVisits,
        String customerUsername,
        String catsitterUsername

) {
}

