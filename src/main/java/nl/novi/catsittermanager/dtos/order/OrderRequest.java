package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record OrderRequest(

        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
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

