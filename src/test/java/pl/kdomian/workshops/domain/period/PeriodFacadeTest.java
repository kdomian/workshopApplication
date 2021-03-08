package pl.kdomian.workshops.domain.period;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kdomian.workshops.domain.event.EventQueryRepository;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeriodFacadeTest {

    @Mock
    PeriodCommandRepository periodCommandRepository;

    PeriodFactory periodFactory = new PeriodFactory();
    @Mock
    SimpleEventEntity event;
    EventDTO eventDTO;
    private LocalDate now;
    private Set<PeriodDTO> periodDTOS;
    private PeriodFacade periodFacade;
    @Mock
    private EventQueryRepository eventQueryRepository;
    @Mock
    private PeriodQueryRepository periodQueryRepository;

    @BeforeEach
    void before() {
        now = LocalDate.now();
        periodDTOS = new LinkedHashSet<>();
        PeriodValidator periodValidator = new PeriodValidator(eventQueryRepository, periodQueryRepository);
        periodFacade = Mockito.spy(new PeriodFacade(periodCommandRepository, periodQueryRepository, periodFactory, periodValidator));


    }

    void initPeriodHintsTests() {
        eventDTO = EventDTO.builder().startDate(now.plusDays(5L)).build();
    }

    @Test
    void getPeriodHintsWithAnyPeriod() {
        //Given
        initPeriodHintsTests();
        //When
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(1, periodHints.size());
        assertEquals(now, periodHints.get(0).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(0).getEndDate());
    }

    @Test
    void getPeriodHintsWithFullScheduledPeriods() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.minusDays(2L)).endDate(now.plusDays(5L)).build());
        when(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(any(), any())).thenReturn(periodDTOS);
        //When
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(0, periodHints.size());
    }

    @Test
    void getPeriodHintsWhenFirstPeriodIsActual() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.minusDays(2L)).endDate(now.plusDays(2L)).build());
        //When
        when(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(any(), any())).thenReturn(periodDTOS);
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(1, periodHints.size());
        assertEquals(now.plusDays(3L), periodHints.get(0).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(0).getEndDate());
    }

    @Test
    void getPeriodHintsWhenFirstPeriodIsAfterNow() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.plusDays(2L)).endDate(now.plusDays(3L)).build());
        //When
        when(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(any(), any())).thenReturn(periodDTOS);
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(2, periodHints.size());
        assertEquals(now, periodHints.get(0).getStartDate());
        assertEquals(now.plusDays(1L), periodHints.get(0).getEndDate());
        assertEquals(now.plusDays(4L), periodHints.get(1).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(1).getEndDate());
    }

    @Test
    void getPeriodHintsWhenManyPeriodsInOneSequenceAndStartSequenceAfterNow() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.plusDays(1L)).endDate(now.plusDays(2L)).build());
        periodDTOS.add(PeriodDTO.builder().startDate(now.plusDays(3L)).endDate(now.plusDays(3L)).build());
        //When
        when(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(any(), any())).thenReturn(periodDTOS);
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(2, periodHints.size());
        assertEquals(now, periodHints.get(0).getStartDate());
        assertEquals(now, periodHints.get(0).getEndDate());
        assertEquals(now.plusDays(4L), periodHints.get(1).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(1).getEndDate());
    }

    @Test
    void getPeriodHintsWhenManyPeriodsInOneSequenceAndStartSequenceBeforeNow() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.minusDays(1L)).endDate(now.plusDays(2L)).build());
        periodDTOS.add(PeriodDTO.builder().startDate(now.plusDays(4L)).endDate(now.plusDays(4L)).build());
        //When
        when(periodQueryRepository.findAllBySimpleEventEntityAndEndDateAfterOrderByStartDateDesc(any(), any())).thenReturn(periodDTOS);
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(eventDTO);
        //Then
        assertEquals(2, periodHints.size());
        assertEquals(now.plusDays(3), periodHints.get(0).getStartDate());
        assertEquals(now.plusDays(3), periodHints.get(0).getEndDate());
        assertEquals(now.plusDays(5L), periodHints.get(1).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(1).getEndDate());
    }

    @Test
    void createPeriodWhenThereIsPeriodWithSameName() {
        //Given
        Set<PeriodDTO> periods = Collections.singleton(PeriodDTO.builder().name("Test1").startDate(now.plusDays(2L)).endDate(now.plusDays(2L)).build());
        when(periodQueryRepository.findAllBySimpleEventEntityOrderByStartDate(event)).thenReturn(periods);
        when(eventQueryRepository.findDtoById(any())).thenReturn(Optional.ofNullable(EventDTO.builder().startDate(now.plusDays(5L)).build()));
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test1").startDate(now).endDate(now).simpleEventEntity(event).build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO));
        //Then
        assertEquals("Couldn't be the same period name on one event", businessException.getMessage());
    }

    @Test
    void createPeriodWhenThereIsCollisions() {
        //Given
        Set<PeriodDTO> periods = Collections.singleton(PeriodDTO.builder().name("Test1").startDate(now.plusDays(2L)).endDate(now.plusDays(4L)).build());
        when(periodQueryRepository.findAllBySimpleEventEntityOrderByStartDate(event)).thenReturn(periods);
        when(eventQueryRepository.findDtoById(any())).thenReturn(Optional.ofNullable(EventDTO.builder().startDate(now.plusDays(5L)).build()));
        PeriodDTO newPeriodDTO = PeriodDTO.builder()
                .name("Test2")
                .startDate(now)
                .endDate(now.plusDays(3L))
                .simpleEventEntity(event)
                .build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(newPeriodDTO));
        //Then
        assertEquals("There is collision with new period on event", businessException.getMessage());
    }

    @Test
    void createPeriodWhenStartDateAfterEndDate() {
        //Given
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test1").startDate(now.plusDays(2L)).endDate(now).simpleEventEntity(event).build();
        when(eventQueryRepository.findDtoById(any())).thenReturn(Optional.ofNullable(EventDTO.builder().startDate(now.plusDays(5L)).build()));
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO));
        //Then
        assertEquals("Start date must be before end date", businessException.getMessage());
    }
}