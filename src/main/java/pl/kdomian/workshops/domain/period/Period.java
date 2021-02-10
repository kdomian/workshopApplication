package pl.kdomian.workshops.domain.period;

import lombok.Getter;
import lombok.Setter;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private SimpleEventEntity simpleEventEntity;


    PeriodDTO toDto() {
        return PeriodDTO.builder()
                .id(id)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .simpleEventEntity(simpleEventEntity)
                .build();
    }
}
