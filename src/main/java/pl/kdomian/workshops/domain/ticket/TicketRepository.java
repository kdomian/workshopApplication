package pl.kdomian.workshops.domain.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByName(String name);
}
