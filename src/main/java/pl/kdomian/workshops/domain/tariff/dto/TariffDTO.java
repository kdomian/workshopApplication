package pl.kdomian.workshops.domain.tariff.dto;

import lombok.Builder;
import lombok.Data;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.ticket.dto.SimpleTicketEntity;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class TariffDTO {
    private Long id;
    @NotNull
    private SimplePeriodEntity simplePeriodEntity;
    @NotNull
    private SimpleTicketEntity simpleTicketEntity;
    @NotNull
    private Double price;
}
