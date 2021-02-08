package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventPeriod")
class PeriodController {

    PeriodFacade periodFacade;

    @DeleteMapping("/{id}")
    void deleteEventPeriod(@PathVariable String id) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PutMapping("/")
    Period editEventPeriod(Period eventPeriod) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<Period> getEventPeriods(Long id) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
