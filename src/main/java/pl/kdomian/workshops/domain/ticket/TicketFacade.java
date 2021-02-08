package pl.kdomian.workshops.domain.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;
import pl.kdomian.workshops.exceptions.BusinessException;
import pl.kdomian.workshops.exceptions.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketFacade {

    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;

    public List<TicketDTO> getTickets() {
        return ticketRepository.findAll().stream().map(Ticket::toDto).collect(Collectors.toList());
    }

    public TicketDTO getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ElementNotFoundException(Entities.TICKET, ticketId));
        return ticket.toDto();
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) throws BusinessException {
        validate(ticketDTO);
        Ticket ticket = ticketFactory.from(ticketDTO);
        return ticketRepository.save(ticket).toDto();
    }

    private void validate(TicketDTO ticketDTO) {
        if (ticketRepository.findByName(ticketDTO.getName()).isPresent())
            throw new BusinessException("Couldn't be crated the same ticket name");
    }

}
