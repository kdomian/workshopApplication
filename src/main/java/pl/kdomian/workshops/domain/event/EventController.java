package pl.kdomian.workshops.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
class EventController {

    private final EventFacade eventFacade;

    @GetMapping("")
    ResponseEntity<List<EventDTO>> getEventsList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventFacade.getEvents());
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
                .body(eventFacade.getEventDto(id));
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

    @PostMapping("/addPeriodToEvent")
    ResponseEntity<PeriodDTO> addPeriodToEvent(@RequestBody @Valid PeriodDTO periodDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventFacade.addPeriodToEvent(periodDTO));
    }

    @GetMapping("/{id}/getEventPeriods")
    ResponseEntity<List<PeriodDTO>> getEventPeriods(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventFacade.getEventPeriods(id));
    }

    @GetMapping("/{id}/getPeriodHits")
    ResponseEntity<List<PeriodDTO>> getPeriodHints(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventFacade.getPeriodHints(id));
    }
}
