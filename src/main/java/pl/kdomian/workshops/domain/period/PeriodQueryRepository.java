package pl.kdomian.workshops.domain.period;

import org.springframework.data.repository.Repository;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;


public interface PeriodQueryRepository extends Repository<Period, Long> {

    Set<PeriodDTO> findAllBySimpleEventEntity(SimpleEventEntity simpleEventEntity);

    Optional<PeriodDTO> findBySimpleEventEntityAndStartDateBeforeAndEndDateAfter(SimpleEventEntity simpleEventEntity, LocalDate now1, LocalDate now2);

    Set<PeriodDTO> findAllBySimpleEventEntityAndEndDateAfter(SimpleEventEntity simpleEventEntity, LocalDate now);
}
