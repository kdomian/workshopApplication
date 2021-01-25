package pl.kdomian.workshops.domain.period;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class PeriodController {

    @DeleteMapping("/eventPeriod/{id}")
    void deleteEventPeriod(@PathVariable String id) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PutMapping("/eventPeriod/")
    Period editEventPeriod(Period eventPeriod) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<Period> getEventPeriods(Long id) {
        throw new IllegalArgumentException("Not implemented yet");
    }
}
