package pl.kdomian.workshops.domain.tariff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.ticket.dto.SimpleTicketEntity;

import java.util.Optional;

@Repository
interface TariffRepository extends JpaRepository<Tariff, Long> {
    Optional<Tariff> findOneFirsBySimplePeriodEntityAndSimpleTicketEntity(SimplePeriodEntity simplePeriodEntity, SimpleTicketEntity simpleTicketEntity);
}
