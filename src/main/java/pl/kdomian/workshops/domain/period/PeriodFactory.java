package pl.kdomian.workshops.domain.period;

import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;

@Service
class PeriodFactory {

    Period from(final PeriodDTO source, final SimpleEventEntity simpleEventEntity) {
        var result = new Period();
        result.setName(source.getName());
        result.setStartDate(source.getStartDate());
        result.setEndDate(source.getEndDate());
        result.setId(source.getId());
        result.setSimpleEventEntity(simpleEventEntity);
        return result;
    }
}
