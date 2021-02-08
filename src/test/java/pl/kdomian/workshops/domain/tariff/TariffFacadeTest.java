package pl.kdomian.workshops.domain.tariff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kdomian.workshops.domain.period.dto.SimplePeriodEntity;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;
import pl.kdomian.workshops.domain.ticket.dto.SimpleTicketEntity;
import pl.kdomian.workshops.exceptions.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TariffFacadeTest {

    static TariffFacade tariffFacade;
    @Mock
    static TariffRepository tariffRepository;
    static List<TariffDTO> tariffDTOList = new ArrayList<>();
    static TariffFactory tariffFactory;

    @BeforeEach
    void init() {
        tariffFactory = new TariffFactory();
        tariffFacade = new TariffFacade(tariffRepository, tariffFactory);
        tariffDTOList.add(TariffDTO.builder().simplePeriodEntity(new SimplePeriodEntity(1L)).simpleTicketEntity(new SimpleTicketEntity(1L)).price(20.0).build());
    }

    @Test
    void createTariffExistingSamePeriodAndTicketTypeShouldTrowsBusinessException() {
        //Given
        TariffDTO tariffDTO = TariffDTO.builder()
                .simplePeriodEntity(new SimplePeriodEntity(1L))
                .simpleTicketEntity(new SimpleTicketEntity(1L))
                .price(20.0)
                .build();
        when(tariffRepository.findOneFirsBySimplePeriodEntityAndSimpleTicketEntity(any(), any())).thenReturn(Optional.of(tariffFactory.from(tariffDTO)));
        //When
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> tariffFacade.createTariff(tariffDTO));
        //Then
        assertEquals("There is already a tariff for the given period and ticket type ", businessException.getMessage());
    }

    @Test
    void createTariff() {
        //Given
        TariffDTO tariffDTO = TariffDTO.builder()
                .simplePeriodEntity(new SimplePeriodEntity(1L))
                .simpleTicketEntity(new SimpleTicketEntity(1L))
                .price(20.0)
                .build();
        Tariff tariff = tariffFactory.from(tariffDTO);
        when(tariffRepository.save(any())).thenReturn(tariff);
        //When
        TariffDTO tariffResponse = assertDoesNotThrow(() -> tariffFacade.createTariff(tariffDTO));
    }
}