package pl.kdomian.workshops.domain.tariff;

import lombok.Getter;
import lombok.Setter;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;
import pl.kdomian.workshops.domain.ticket.dto.SimpleTicketEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PERIOD_ID")
    private SimplePeriodEntity simplePeriodEntity;
    @ManyToOne
    @JoinColumn(name = "TICKET_ID")
    private SimpleTicketEntity simpleTicketEntity;
    private Double price;

    TariffDTO toDto() {
        return TariffDTO.builder()
                .id(id)
                .simplePeriodEntity(simplePeriodEntity)
                .simpleTicketEntity(simpleTicketEntity)
                .price(price)
                .build();
    }
}
