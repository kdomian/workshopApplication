package pl.kdomian.workshops.domain.ticket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TICKET")
@Getter
@NoArgsConstructor
public class SimpleTicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SimpleTicketEntity(Long id) {
        this.id = id;
    }

}
