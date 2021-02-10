package pl.kdomian.workshops.domain.event;

import org.springframework.data.repository.Repository;
import pl.kdomian.workshops.domain.event.dto.EventDTO;

import java.util.List;
import java.util.Optional;

public interface EventQueryRepository extends Repository<Event, Long> {

    List<Event> findAll();

    Optional<EventDTO> findDtoById(Long id);
}
