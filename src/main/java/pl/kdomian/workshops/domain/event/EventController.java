package pl.kdomian.workshops.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.PeriodQueryRepository;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;
import pl.kdomian.workshops.exceptions.ElementNotFoundException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
class EventController {

    private final EventFacade eventFacade;
    private final EventQueryRepository eventQueryRepository;
    private final PeriodQueryRepository periodQueryRepository;

    @GetMapping("")
    ResponseEntity<List<EventDTO>> getEventsList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ArrayList<>(eventQueryRepository.findByOrderByStartDate()));
    }

    @PostMapping("")
    ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventFacade.createEvent(eventDTO));
    }

    @GetMapping("/{id}")
    ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventQueryRepository.findDtoById(id).orElseThrow(() -> new ElementNotFoundException(Entities.EVENT, id)));
    }

    @PutMapping("/{id}")
    ResponseEntity<EventDTO> editEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(eventFacade.editEvent(id, eventDTO).toDto());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventFacade.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/getCurrentEventPeriod")
    ResponseEntity<PeriodDTO> getCurrentEventPeriod(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(periodQueryRepository.findBySimpleEventEntityAndStartDateBeforeAndEndDateAfter(new SimpleEventEntity(id), LocalDate.now(), LocalDate.now())
                        .orElseThrow(() -> new BusinessException("Couldn't find current period for Event with id: " + id)));
    }

    @GetMapping("/{id}/getPeriodHits")
    ResponseEntity<List<PeriodDTO>> getPeriodHints(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventFacade.getPeriodHints(id));
    }

    @GetMapping("{id}/getActivePeriods")
    ResponseEntity<List<PeriodDTO>> getActivePeriods(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ArrayList<>(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(new SimpleEventEntity(id), LocalDate.now())));
    }

    @GetMapping("/{id}/activate")
    ResponseEntity<EventDTO> activateEvent(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(eventFacade.activateEvent(id));
    }
}
