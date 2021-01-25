package pl.kdomian.workshops.domain.event;

import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.event.dto.EventDTO;

@Service
class EventFactory {
    Event from(final EventDTO source) {
        var result = new Event();
        result.setId(source.getId());
        result.setName(source.getName());
        result.setStartDate(source.getStartDate());
        result.setEndDate(source.getEndDate());
        result.setIsActive(source.getIsActive() != null ? source.getIsActive() : Boolean.FALSE);
        return result;
    }
}
