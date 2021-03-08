package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.TariffQueryRepository;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;
import pl.kdomian.workshops.exceptions.ElementNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/periods")
class PeriodController {

    private final PeriodFacade periodFacade;
    private final PeriodQueryRepository periodQueryRepository;
    private final TariffQueryRepository tariffQueryRepository;
    private final PeriodCommandRepository periodCommandRepository;

    @PostMapping("")
    ResponseEntity<PeriodDTO> createPeriod(@RequestBody @Valid PeriodDTO periodDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(periodFacade.save(periodDTO));
    }

    @GetMapping("/{id}")
    ResponseEntity<PeriodDTO> getPeriod(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(periodCommandRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(Entities.PERIOD, id)).toDto());
    }

    @GetMapping("/getEventPeriods/{id}")
    ResponseEntity<List<PeriodDTO>> getEventPeriods(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ArrayList<>(periodQueryRepository.findAllBySimpleEventEntityOrderByStartDate(new SimpleEventEntity(id))));
    }

    @GetMapping("/{id}/getPeriodTariffs")
    ResponseEntity<List<TariffDTO>> getPeriodTariffs(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ArrayList<>(tariffQueryRepository.findAllBySimplePeriodEntity(new SimplePeriodEntity(id))));
    }

}
