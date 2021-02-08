package pl.kdomian.workshops.domain.ticket;

import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;

@Service
class TicketFactory {
    Ticket from(final TicketDTO source) {
        var result = new Ticket();
        result.setName(source.getName());
        result.setTicketType(source.getTicketType());
        result.setGender(source.getGender());
        result.setIsBalanced(source.getIsBalanced());
        return result;
    }
}
