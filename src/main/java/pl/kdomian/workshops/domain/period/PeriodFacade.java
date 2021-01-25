package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeriodFacade {

    private final PeriodRepository periodRepository;
    private final PeriodFactory periodFactory;

    public PeriodDTO save(PeriodDTO periodDTO, SimpleEventEntity simpleEventEntity) {
        if (periodDTO.getEndDate().isAfter(simpleEventEntity.getStartDate())) {
            throw new BusinessException("New event period could not finish after event start date");
        }
        Period newPeriod = periodFactory.from(periodDTO, simpleEventEntity);
        newPeriod.validateStartEndDatePeriod();
        periodRepository.findAllBySimpleEventEntity(simpleEventEntity).stream()
                .noneMatch(period -> !period.validateCorectPeriodSeparable(newPeriod) || !period.validatePeriodName(newPeriod));
        return periodRepository.save(newPeriod).toDto();

    }

    public void delete(PeriodDTO periodDTO, SimpleEventEntity simpleEventEntity) {
        periodRepository.delete(periodFactory.from(periodDTO, simpleEventEntity));
    }

    public List<PeriodDTO> getPeriodsByEventId(SimpleEventEntity simpleEventEntity) {
        return periodRepository.findAllBySimpleEventEntity(simpleEventEntity).stream().map(Period::toDto).collect(Collectors.toList());
    }

    public List<PeriodDTO> getPeriodHints(SimpleEventEntity event) {
        List<PeriodDTO> periodHintsList = new ArrayList<>();
        List<PeriodDTO> periodDTOS = getActualAndFuturePeriodsForEvent(event);

        if (periodDTOS.isEmpty()) {
            periodHintsList.add(createNewPeriodHints(LocalDate.now(), event.getStartDate(), event.getStartDate()));
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
                    tempPeriod = createNewPeriodHints(periodDTO.getEndDate().plusDays(1L), null, event.getStartDate());
                }
            } else {
                if (LocalDate.now().isBefore(periodDTO.getStartDate()))
                    periodHintsList.add(createNewPeriodHints(LocalDate.now(), periodDTO.getStartDate().minusDays(1L), event.getStartDate()));
                tempPeriod = createNewPeriodHints(periodDTO.getEndDate().plusDays(1L), null, event.getStartDate());
            }
        if (tempPeriod != null) {
            tempPeriod.setEndDate(event.getStartDate());
            periodHintsList.add(tempPeriod);
        }

        return periodHintsList;
    }

    private List<PeriodDTO> getActualAndFuturePeriodsForEvent(SimpleEventEntity event) {
        return getPeriodsByEventId(event).stream()
                .filter(periodDTO -> LocalDate.now().isBefore(periodDTO.getEndDate()))
                .sorted(Comparator.comparing(PeriodDTO::getStartDate))
                .collect(Collectors.toList());
    }

    private PeriodDTO createNewPeriodHints(LocalDate startDate, LocalDate endDate, LocalDate eventStartDate) {
        if ((endDate != null && eventStartDate.isBefore(endDate)) || (startDate != null && eventStartDate.isBefore(startDate)))
            return null;
        return PeriodDTO.builder().startDate(startDate).endDate(endDate).build();
    }
}
