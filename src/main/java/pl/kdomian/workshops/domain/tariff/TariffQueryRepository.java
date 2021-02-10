package pl.kdomian.workshops.domain.tariff;

import org.springframework.data.repository.Repository;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;

import java.util.List;

public interface TariffQueryRepository extends Repository<Tariff, Long> {
    List<TariffDTO> findAllBySimplePeriodEntity(SimplePeriodEntity simplePeriodEntity);
}
