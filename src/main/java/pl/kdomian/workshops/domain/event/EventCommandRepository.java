package pl.kdomian.workshops.domain.event;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface EventCommandRepository extends Repository<Event, Long> {

    Optional<Event> findById(Long eventId);

    Event save(Event event);

    void deleteById(Long id);
}
