package pl.kdomian.workshops.domain.event;

import org.springframework.data.repository.Repository;
import pl.kdomian.workshops.domain.event.dto.EventDTO;

import java.util.Optional;
import java.util.Set;

public interface EventQueryRepository extends Repository<Event, Long> {

    Set<EventDTO> findBy();

    Optional<EventDTO> findDtoById(Long id);
}
