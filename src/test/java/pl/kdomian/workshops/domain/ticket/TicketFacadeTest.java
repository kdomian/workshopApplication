package pl.kdomian.workshops.domain.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kdomian.workshops.domain.ticket.dto.TicketDTO;
import pl.kdomian.workshops.exceptions.BusinessException;
import pl.kdomian.workshops.utils.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TicketFacadeTest {

    static TicketFacade ticketFacade;
    @Mock
    static TicketRepository ticketRepository;
    static List<TicketDTO> ticketDTOList = new ArrayList<>();
    private TicketFactory ticketFactory;

    @BeforeEach
    void init() {
        ticketFactory = new TicketFactory();
        ticketFacade = new TicketFacade(ticketRepository, ticketFactory);
        ticketDTOList.add(TicketDTO.builder().name("1-name").ticketType(TicketType.COUPLE).gender(Gender.MALE).isBalanced(true).build());
        ticketDTOList.add(TicketDTO.builder().name("2-name").ticketType(TicketType.PARTY).gender(Gender.FEMALE).isBalanced(false).build());
        ticketDTOList.add(TicketDTO.builder().name("3-name").ticketType(TicketType.SINGLE).gender(Gender.MALE).isBalanced(true).build());
    }


    @Test
    void createNewTicket() {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .name("Test_ticket_name")
                .ticketType(TicketType.COUPLE)
                .gender(Gender.MALE)
                .isBalanced(false)
                .build();
        when(ticketRepository.save(any())).thenReturn(ticketFactory.from(ticketDTO));
        //When
        TicketDTO ticket = ticketFacade.createTicket(ticketDTO);
        //Then
        assertEquals(ticket, ticketDTO);
    }

    @Test
    void createNewTicketWitExistingTicketNameShouldNotBePossible() {
        //Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .name("1-name")
                .ticketType(TicketType.COUPLE)
                .gender(Gender.MALE)
                .isBalanced(false)
                .build();
        when(ticketRepository.findByName(any())).thenReturn(Optional.ofNullable(ticketFactory.from(ticketDTO)));
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> ticketFacade.createTicket(ticketDTO));
        //Then
        assertEquals("Couldn't be crated the same ticket name", businessException.getMessage());

    }
}