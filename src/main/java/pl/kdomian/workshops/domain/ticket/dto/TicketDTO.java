package pl.kdomian.workshops.domain.ticket.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.kdomian.workshops.domain.ticket.TicketType;
import pl.kdomian.workshops.utils.Gender;

import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode
public class TicketDTO {
    private Long id;
    @NotNull
    private String name;
    private Gender gender;
    @NotNull
    private TicketType ticketType;
    private Boolean isBalanced;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String name, Gender gender, TicketType ticketType, Boolean isBalanced) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.ticketType = ticketType;
        this.isBalanced = isBalanced;
    }
}
