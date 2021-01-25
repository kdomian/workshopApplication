package pl.kdomian.workshops.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EventCommandRepository extends JpaRepository<Event, Long> {
}
