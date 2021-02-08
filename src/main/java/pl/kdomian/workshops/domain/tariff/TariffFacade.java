package pl.kdomian.workshops.domain.tariff;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;
import pl.kdomian.workshops.exceptions.BusinessException;
import pl.kdomian.workshops.exceptions.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffFacade {

    private final TariffRepository tariffRepository;
    private final TariffFactory tariffFactory;

    public List<TariffDTO> getTariffs() {
        return tariffRepository.findAll().stream().map(Tariff::toDto).collect(Collectors.toList());
    }

    public TariffDTO getTariff(Long tariffId) {
        Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(() -> new ElementNotFoundException(Entities.TARIFF, tariffId));
        return tariff.toDto();
    }


    public TariffDTO createTariff(TariffDTO tariffDTO) {
        validate(tariffDTO);
        Tariff tariff = tariffFactory.from(tariffDTO);
        return tariffRepository.save(tariff).toDto();
    }

    private void validate(TariffDTO tariffDTO) {
        if (tariffRepository.findOneFirsBySimplePeriodEntityAndSimpleTicketEntity(tariffDTO.getSimplePeriodEntity(), tariffDTO.getSimpleTicketEntity()).isPresent())
            throw new BusinessException("There is already a tariff for the given period and ticket type ");
    }

}
