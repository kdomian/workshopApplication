package pl.kdomian.workshops.domain.period.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class PeriodDTO {
    private Long id;
    @NotEmpty(message = "Period name will be not null")
    private String name;
    @NotNull(message = "Start date will be not null")
    @Future(message = "Start date will be from future")
    private LocalDate startDate;
    @NotNull(message = "End date will be not null")
    @Future(message = "End date will be from future")
    private LocalDate endDate;
}
