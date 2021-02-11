package pl.kdomian.workshops.domain.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.kdomian.workshops.domain.event.dto.EventDTO;
import pl.kdomian.workshops.domain.period.PeriodFacade;
import pl.kdomian.workshops.domain.period.PeriodQueryRepository;
import pl.kdomian.workshops.exceptions.ExceptionAdviceHandler;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest {

    private MockMvc mvc;
    @Mock
    private EventCommandRepository eventCommandRepository;
    @Mock
    private EventQueryRepository eventQueryRepository;
    private EventFactory eventFactory;
    @Mock
    private PeriodFacade periodFacade;
    @Mock
    private PeriodQueryRepository periodQueryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventFactory = new EventFactory();
        EventFacade eventFacade = new EventFacade(eventCommandRepository, eventFactory, periodFacade);
        EventController eventController = new EventController(eventFacade, eventQueryRepository, periodQueryRepository);
        mvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new ExceptionAdviceHandler())
                .build();
    }

    @Test
    public void getAllEvents() throws Exception {
        mvc.perform(get("/events"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getEventByIdWhenExist() throws Exception {
        //Given
        EventDTO eventDTO = EventDTO.builder().name("Event name").startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1L)).build();
        when(eventQueryRepository.findDtoById(1L)).thenReturn(Optional.ofNullable(eventDTO));
        //When
        //Then
        mvc.perform(get("/events/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getEventByIdWhenNotExist() throws Exception {
        //Given
        when(eventCommandRepository.findById(1L)).thenReturn(Optional.empty());
        //When
        //Then
        mvc.perform(get("/events/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}