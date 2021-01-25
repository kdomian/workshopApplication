package pl.kdomian.workshops.domain.period;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;

import java.util.List;

@Repository
interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAllBySimpleEventEntity(SimpleEventEntity simpleEventEntity);
}
