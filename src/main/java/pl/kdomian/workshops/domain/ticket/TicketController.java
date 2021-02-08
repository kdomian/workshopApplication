package pl.kdomian.workshops.domain.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
class TicketController {

    private final TicketFacade ticketFacade;

    @GetMapping("")
    ResponseEntity<List<TicketDTO>> getTicketList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketFacade.getTickets());
    }

    @GetMapping("/{id}")
    ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketFacade.getTicket(id));
    }

    @PostMapping("")
    ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketFacade.createTicket(ticketDTO));
    }

}
