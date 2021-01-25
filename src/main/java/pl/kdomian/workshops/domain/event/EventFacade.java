package pl.kdomian.workshops.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.period.PeriodFacade;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;
import pl.kdomian.workshops.exceptions.ElementNotFoundException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventFacade {

    private final EventCommandRepository eventCommandRepository;
    private final EventFactory eventFactory;
    private final PeriodFacade periodFacade;

    private Event getEvent(Long eventId) {
        return eventCommandRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException(Entities.EVENT, eventId));
    }

    public List<EventDTO> getEvents() {
        return eventCommandRepository.findAll().stream()
                .map(Event::toDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventDto(Long eventId) {
        return getEvent(eventId).toDto();
    }

    public EventDTO createEvent(@Valid EventDTO eventDTO) {
        var event = eventFactory.from(eventDTO);
        if (event.isValidStartAndEndDate())
            return eventCommandRepository.save(event).toDto();
        else
            throw new BusinessException(" Dates in event are not valid ");
    }

    @Transactional
    public Event editEvent(Long eventId, EventDTO eventDTO) {
        var editedEvent = getEvent(eventId);
        editedEvent.setName(eventDTO.getName());
        editedEvent.setStartDate(eventDTO.getStartDate());
        editedEvent.setEndDate(eventDTO.getEndDate());
        editedEvent.setIsActive(eventDTO.getIsActive());
        return eventCommandRepository.save(editedEvent);
    }

    public void deleteEvent(Long id) {
        eventCommandRepository.deleteById(id);
    }

    public PeriodDTO addPeriodToEvent(Long eventId, @Valid PeriodDTO periodDTO) {
        return periodFacade.save(periodDTO, getEvent(eventId).toSimpleEventEnityt());
    }

    public void deletePeriodFromEvent(Long eventId, PeriodDTO periodDTO) {
        periodFacade.delete(periodDTO, getEvent(eventId).toSimpleEventEnityt());
    }

    public List<PeriodDTO> getEventPeriods(Long eventId) {
        return periodFacade.getPeriodsByEventId(getEvent(eventId).toSimpleEventEnityt());
    }

    public List<PeriodDTO> getPeriodHints(Long eventId) {
        return periodFacade.getPeriodHints(getEvent((eventId)).toSimpleEventEnityt());
    }
}
