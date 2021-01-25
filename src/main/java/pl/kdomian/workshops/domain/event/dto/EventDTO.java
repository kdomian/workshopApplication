package pl.kdomian.workshops.domain.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EventDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

}
