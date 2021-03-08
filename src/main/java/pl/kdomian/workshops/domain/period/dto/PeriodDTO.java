package pl.kdomian.workshops.domain.period.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class PeriodDTO {
    private Long id;
    @NotEmpty(message = "Period name will be not null")
    private String name;
    @NotNull(message = "Start date will be not null")
    @Future(message = "Start date will be from future")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate startDate;
    @NotNull(message = "End date will be not null")
    @Future(message = "End date will be from future")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate endDate;
    private SimpleEventEntity simpleEventEntity;
}
