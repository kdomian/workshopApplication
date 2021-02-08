package pl.kdomian.workshops.domain.event;

import lombok.Getter;
import lombok.Setter;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @Future
    private LocalDate startDate;
    @NotNull
    @Future
    private LocalDate endDate;
    @NotNull
    private Boolean isActive;


    EventDTO toDto() {
        return EventDTO.builder()
                .id(id)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(isActive)
                .build();
    }

    boolean isValidStartAndEndDate() {
        return this.startDate != null
                && this.endDate != null
                && this.startDate.isBefore(this.endDate);
    }

    public SimpleEventEntity toSimpleEventEntity() {
        return new SimpleEventEntity(id);
    }
}
