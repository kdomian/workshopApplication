package pl.kdomian.workshops.domain.period;

import lombok.Getter;
import lombok.Setter;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;

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
                .build();
    }

    Boolean validateCorectPeriodSeparable(Period period) {
        if (period.startDate.isAfter(this.endDate) || period.endDate.isBefore(this.startDate))
            return true;
        else throw new BusinessException("There is collision with new period on event");
    }

    Boolean validateStartEndDatePeriod() {
        if (startDate.isBefore(endDate) || startDate.equals(endDate))
            return true;
        else throw new BusinessException("Start date must be before end date");
    }

    Boolean validatePeriodName(Period period) {
        if (!period.name.equalsIgnoreCase(this.name))
            return true;
        else throw new BusinessException("Couldn't be the same period name on one event");
    }
}
