package pl.kdomian.workshops.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.kdomian.workshops.Entities;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;
import pl.kdomian.workshops.exceptions.ExceptionAdviceHandler;
import pl.kdomian.workshops.utils.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TicketControllerTest {

    MockMvc mvc;
    @Mock
    TicketRepository ticketRepository;
    TicketFactory ticketFactory;


    @BeforeEach
    public void setUp() {
        ticketFactory = new TicketFactory();
        MockitoAnnotations.initMocks(this);
        TicketFacade ticketFacade = new TicketFacade(ticketRepository, ticketFactory);
        TicketController ticketController = new TicketController(ticketFacade);
        mvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new ExceptionAdviceHandler())
                .build();
    }

    @Test
    void getAllTicketType() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setName("Test ticket");
        ticket.setGender(Gender.MALE);
        ticket.setIsBalanced(false);
        ticket.setTicketType(TicketType.COUPLE);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        when(ticketRepository.findAll()).thenReturn(tickets);

        mvc.perform(get("/ticket"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gender", Matchers.is(Gender.MALE.toString())));
    }

    @Test
    void getTicketTypeWhenExist() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setName("Test ticket");
        ticket.setGender(Gender.MALE);
        ticket.setIsBalanced(false);
        ticket.setTicketType(TicketType.COUPLE);
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));

        mvc.perform(get("/ticket/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Test ticket")));
    }

    @Test
    void getTicketTypeWhenNotExist() throws Exception {
        when(ticketRepository.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/ticket/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.is("Could not find " + Entities.TICKET.toString() + " with id: " + 1)));
    }

    @Test
    void createTicketShouldReturnStatusOk() throws Exception {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .name("Test_name")
                .ticketType(TicketType.COUPLE)
                .gender(Gender.MALE)
                .isBalanced(true)
                .build();
        Ticket ticket = ticketFactory.from(ticketDTO);
        when(ticketRepository.findByName(any())).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(ticketDTO);
        //When
        mvc.perform(post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createTicketWithTheSameNameShouldReturnBadRequest() throws Exception {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .name("Test_name")
                .ticketType(TicketType.COUPLE)
                .gender(Gender.MALE)
                .isBalanced(true)
                .build();
        Ticket ticket = ticketFactory.from(ticketDTO);
        when(ticketRepository.findByName(any())).thenReturn(Optional.ofNullable(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(ticketDTO);
        //When
        mvc.perform(post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicketWithoutNameShouldReturnBadRequest() throws Exception {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .ticketType(TicketType.COUPLE)
                .gender(Gender.MALE)
                .isBalanced(true)
                .build();
        when(ticketRepository.findByName(any())).thenReturn(Optional.empty());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(ticketDTO);
        //When
        mvc.perform(post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicketWithoutTicketTypeShouldReturnBadRequest() throws Exception {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .name("Test_name")
                .gender(Gender.MALE)
                .isBalanced(true)
                .build();
        when(ticketRepository.findByName(any())).thenReturn(Optional.empty());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(ticketDTO);
        //When
        mvc.perform(post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}