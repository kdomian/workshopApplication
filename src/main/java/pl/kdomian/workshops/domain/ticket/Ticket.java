package pl.kdomian.workshops.domain.ticket;

import lombok.Getter;
import lombok.Setter;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;
import pl.kdomian.workshops.utils.Gender;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Gender gender;
    private TicketType ticketType;
    private Boolean isBalanced;

    TicketDTO toDto() {
        return TicketDTO.builder()
                .id(id)
                .name(name)
                .gender(gender)
                .ticketType(ticketType)
                .isBalanced(isBalanced)
                .build();
    }
}
