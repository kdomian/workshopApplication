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

@Service
@RequiredArgsConstructor
public class EventFacade {

    private final EventCommandRepository eventCommandRepository;
    private final EventQueryRepository eventQueryRepository;
    private final EventFactory eventFactory;
    private final PeriodFacade periodFacade;

    public EventDTO createEvent(@Valid EventDTO eventDTO) {
        var event = eventFactory.from(eventDTO);
        if (event.isValidStartAndEndDate())
            return eventCommandRepository.save(event).toDto();
        else
            throw new BusinessException(" Dates in event are not valid ");
    }

    @Transactional
    public Event editEvent(Long eventId, EventDTO eventDTO) {
        Event editedEvent = eventCommandRepository.findById(eventId).orElseThrow(() -> new ElementNotFoundException(Entities.EVENT, eventId));
        editedEvent.setName(eventDTO.getName());
        editedEvent.setStartDate(eventDTO.getStartDate());
        editedEvent.setEndDate(eventDTO.getEndDate());
        editedEvent.setIsActive(eventDTO.getIsActive());
        return eventCommandRepository.save(editedEvent);
    }

    public void deleteEvent(Long id) {
        eventCommandRepository.deleteById(id);
    }

    public List<PeriodDTO> getPeriodHints(Long eventId) {
        EventDTO eventDTO = eventQueryRepository.findDtoById(eventId).orElseThrow(() -> new ElementNotFoundException(Entities.EVENT, eventId));
        return periodFacade.getPeriodHints(eventDTO);
    }

    public EventDTO activateEvent(Long eventId) {
        Event editedEvent = eventCommandRepository.findById(eventId).orElseThrow(() -> new ElementNotFoundException(Entities.EVENT, eventId));
        editedEvent.setIsActive(true);
        return eventCommandRepository.save(editedEvent).toDto();
    }
}
