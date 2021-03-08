package pl.kdomian.workshops.domain.tariff;

import org.springframework.data.repository.Repository;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;

import java.util.Set;

public interface TariffQueryRepository extends Repository<Tariff, Long> {
    Set<TariffDTO> findAllBySimplePeriodEntity(SimplePeriodEntity simplePeriodEntity);
}
