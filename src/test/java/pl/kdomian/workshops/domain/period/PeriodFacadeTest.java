package pl.kdomian.workshops.domain.period;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kdomian.workshops.domain.event.dto.SimpleEventEntity;
import pl.kdomian.workshops.domain.period.dto.PeriodDTO;
import pl.kdomian.workshops.exceptions.BusinessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeriodFacadeTest {

    @Mock
    PeriodRepository periodRepository;

    PeriodFactory periodFactory;
    @Mock
    SimpleEventEntity event;
    private LocalDate now;
    private List<PeriodDTO> periodDTOS;
    private PeriodFacade periodFacade;

    @BeforeEach
    void before() {
        now = LocalDate.now();
        periodDTOS = new ArrayList<>();
        periodFactory = new PeriodFactory();
        periodFacade = Mockito.spy(new PeriodFacade(periodRepository, periodFactory));
        when(event.getStartDate()).thenReturn(now.plusDays(5L));


    }

    void initPeriodHintsTests() {
        doReturn(periodDTOS).when(periodFacade).getPeriodsByEventId(event);
    }

    @Test
    void getPeriodHintsWithAnyPeriod() {
        //Given
        initPeriodHintsTests();
        //When
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(event);
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
        //When
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(event);
        //Then
        assertEquals(0, periodHints.size());
    }

    @Test
    void getPeriodHintsWhenFirstPeriodIsActual() {
        //Given
        initPeriodHintsTests();
        periodDTOS.add(PeriodDTO.builder().startDate(now.minusDays(2L)).endDate(now.plusDays(2L)).build());
        //When
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(event);
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
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(event);
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
        List<PeriodDTO> periodHints = periodFacade.getPeriodHints(event);
        //Then
        assertEquals(2, periodHints.size());
        assertEquals(now, periodHints.get(0).getStartDate());
        assertEquals(now, periodHints.get(0).getEndDate());
        assertEquals(now.plusDays(4L), periodHints.get(1).getStartDate());
        assertEquals(now.plusDays(5L), periodHints.get(1).getEndDate());
    }

    @Test
    void createPeriodWhenThereIsPeriodWithSameName() {
        //Given
        List<Period> periods = new ArrayList<>();
        Period period = new Period();
        period.setName("Test1");
        period.setStartDate(now.plusDays(2L));
        period.setEndDate(now.plusDays(2L));
        periods.add(period);
        when(periodRepository.findAllBySimpleEventEntity(event)).thenReturn(periods);
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test1").startDate(now).endDate(now).build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO, event));
        //Then
        assertEquals("Couldn't be the same period name on one event", businessException.getMessage());
    }

    @Test
    void createPeriodWhenThereIsCollisions() {
        //Given
        List<Period> periods = new ArrayList<>();
        Period period = new Period();
        period.setName("Test1");
        period.setStartDate(now.plusDays(2L));
        period.setEndDate(now.plusDays(4L));
        periods.add(period);
        when(periodRepository.findAllBySimpleEventEntity(event)).thenReturn(periods);
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test2").startDate(now).endDate(now.plusDays(3L)).build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO, event));
        //Then
        assertEquals("There is collision with new period on event", businessException.getMessage());
    }

    @Test
    void createPeriodWhenStartDateAfterEndDate() {
        //Given
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test1").startDate(now.plusDays(2L)).endDate(now).build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO, event));
        //Then
        assertEquals("Start date must be before end date", businessException.getMessage());
    }

    @Test
    void createPeriodAfterEventStartDate() {
        //Given
        when(event.getStartDate()).thenReturn(now.plusDays(5L));
        PeriodDTO periodDTO = PeriodDTO.builder().name("Test1").startDate(now.plusDays(7L)).endDate(now.plusDays(8L)).build();
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> periodFacade.save(periodDTO, event));
        //Then
        assertEquals("New event period could not finish after event start date", businessException.getMessage());
    }
}