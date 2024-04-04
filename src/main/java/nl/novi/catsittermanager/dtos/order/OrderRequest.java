package nl.novi.catsittermanager.dtos.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record OrderRequest(

        @Future
        LocalDate startDate,
        @Future
        LocalDate endDate,
        @Positive
        int dailyNumberOfVisits,
        @Positive
        int totalNumberOfVisits,
        String customerUsername,
        String catsitterUsername

) {
}

