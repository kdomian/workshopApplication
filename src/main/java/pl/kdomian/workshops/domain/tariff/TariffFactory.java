package pl.kdomian.workshops.domain.tariff;

import org.springframework.stereotype.Service;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;

@Service
class TariffFactory {
    Tariff from(TariffDTO source) {
        var result = new Tariff();
        result.setId(source.getId());
        result.setPrice(source.getPrice());
        result.setSimplePeriodEntity(source.getSimplePeriodEntity());
        result.setSimpleTicketEntity(source.getSimpleTicketEntity());
        return result;
    }
}
