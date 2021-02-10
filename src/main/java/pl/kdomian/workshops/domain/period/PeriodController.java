package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.TariffQueryRepository;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventPeriod")
class PeriodController {

    private final PeriodFacade periodFacade;
    private final TariffQueryRepository tariffQueryRepository;

    @PostMapping("/createPeriod")
    ResponseEntity<PeriodDTO> createPeriod(@RequestBody @Valid PeriodDTO periodDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(periodFacade.save(periodDTO));
    }

    @GetMapping("/{id}/getPeriodTariffs")
    ResponseEntity<List<TariffDTO>> getPeriodTariffs(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tariffQueryRepository.findAllBySimplePeriodEntity(new SimplePeriodEntity(id)));
    }

}
