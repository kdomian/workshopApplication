package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.event.EventQueryRepository;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class PeriodValidator {

    private final EventQueryRepository eventQueryRepository;
    private final PeriodQueryRepository periodQueryRepository;

    void validation(PeriodDTO periodDTO) {
        validateEndDateAfterEventStartDate(periodDTO);
        validateStartEndDatePeriod(periodDTO);
        validatePeriodSeparable(periodDTO);
    }

    private void validatePeriodSeparable(PeriodDTO periodDTO) {
        periodQueryRepository.findAllBySimpleEventEntityOrderByStartDate(periodDTO.getSimpleEventEntity()).stream()
                .forEach(persistPeriod -> {
                    validatePeriodCollision(periodDTO, persistPeriod);
                    validatePeriodName(periodDTO, persistPeriod);
                });
    }

    void validatePeriodCollision(PeriodDTO newPeriod, PeriodDTO persistsPeriod) {
        LocalDate startDate = newPeriod.getStartDate();
        LocalDate endDate = newPeriod.getEndDate();
        LocalDate startDatePersist = persistsPeriod.getStartDate();
        LocalDate endDatePersist = persistsPeriod.getEndDate();
        if ((startDate.isAfter(startDatePersist) && startDate.isBefore(endDatePersist)) ||
                (endDate.isAfter(startDatePersist) && endDate.isBefore(endDatePersist)) ||
                startDate.equals(startDatePersist) ||
                startDate.equals(endDatePersist) ||
                endDate.equals(startDatePersist) ||
                endDate.equals(endDatePersist)
        )
            throw new BusinessException("There is collision with new period on event");
    }

    private void validatePeriodName(PeriodDTO newPeriod, PeriodDTO persistsPeriod) {
        if (persistsPeriod.getName().equals(newPeriod.getName()))
            throw new BusinessException("Couldn't be the same period name on one event");
    }

    void validateEndDateAfterEventStartDate(PeriodDTO periodDTO) {
        Optional<EventDTO> eventDto = eventQueryRepository.findDtoById(periodDTO.getSimpleEventEntity().getId());
        if (periodDTO.getEndDate().isAfter(eventDto.get().getStartDate()))
            throw new BusinessException("New event period could not finish after event start date");
    }

    void validateStartEndDatePeriod(PeriodDTO periodDTO) {
        if (periodDTO.getStartDate().isAfter(periodDTO.getEndDate()))
            throw new BusinessException("Start date must be before end date");
    }
}