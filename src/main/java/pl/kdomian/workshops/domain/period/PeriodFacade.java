package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodFacade {

    private final PeriodCommandRepository periodCommandRepository;
    private final PeriodQueryRepository periodQueryRepository;
    private final PeriodFactory periodFactory;
    private final PeriodValidator periodValidator;

    public PeriodDTO save(PeriodDTO periodDTO) {
        periodValidator.validation(periodDTO);
        Period period = periodFactory.from(periodDTO);
        return periodCommandRepository.save(period).toDto();
    }

    public void delete(PeriodDTO periodDTO) {
        periodCommandRepository.delete(periodFactory.from(periodDTO));
    }

    public List<PeriodDTO> getPeriodsByEventId(SimpleEventEntity simpleEventEntity) {
        return new ArrayList<>(periodQueryRepository.findAllBySimpleEventEntityOrderByStartDate(simpleEventEntity));
    }

    public List<PeriodDTO> getPeriodHints(EventDTO eventDTO) {
        List<PeriodDTO> periodHintsList = new ArrayList<>();
        List<PeriodDTO> periodDTOS = new ArrayList<>(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(new SimpleEventEntity(eventDTO.getId()), LocalDate.now()));

        if (periodDTOS.isEmpty()) {
            periodHintsList.add(createNewPeriodHints(LocalDate.now(), eventDTO.getStartDate(), eventDTO.getStartDate()));
            return periodHintsList;
        }

        PeriodDTO tempPeriod = null;
        for (PeriodDTO periodDTO : periodDTOS)
            if (tempPeriod != null) {
                if (tempPeriod.getStartDate().equals(periodDTO.getStartDate())) {
                    tempPeriod.setStartDate(periodDTO.getEndDate().plusDays(1L));
                } else {
                    tempPeriod.setEndDate(periodDTO.getStartDate().minusDays(1L));
                    periodHintsList.add(tempPeriod);
                    tempPeriod = createNewPeriodHints(periodDTO.getEndDate().plusDays(1L), null, eventDTO.getStartDate());
                }
            } else {
                if (LocalDate.now().isBefore(periodDTO.getStartDate()))
                    periodHintsList.add(createNewPeriodHints(LocalDate.now(), periodDTO.getStartDate().minusDays(1L), eventDTO.getStartDate()));
                tempPeriod = createNewPeriodHints(periodDTO.getEndDate().plusDays(1L), null, eventDTO.getStartDate());
            }
        if (tempPeriod != null) {
            tempPeriod.setEndDate(eventDTO.getStartDate());
            periodHintsList.add(tempPeriod);
        }

        return periodHintsList;
    }

    private PeriodDTO createNewPeriodHints(LocalDate startDate, LocalDate endDate, LocalDate eventStartDate) {
        if ((endDate != null && eventStartDate.isBefore(endDate)) || (startDate != null && eventStartDate.isBefore(startDate)))
            return null;
        return PeriodDTO.builder().startDate(startDate).endDate(endDate).build();
    }
}
