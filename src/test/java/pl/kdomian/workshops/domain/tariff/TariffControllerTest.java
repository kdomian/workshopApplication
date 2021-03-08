package pl.kdomian.workshops.domain.tariff;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.kdomian.workshops.exceptions.ExceptionAdviceHandler;

class TariffControllerTest {

    MockMvc mvc;
    @Mock
    TariffRepository tariffRepository;
    @Mock
    TariffQueryRepository tariffQueryRepository;
    TariffFactory tariffFactory;


    @BeforeEach
    public void setUp() {
        tariffFactory = new TariffFactory();
        MockitoAnnotations.initMocks(this);
        TariffFacade tariffFacade = new TariffFacade(tariffRepository, tariffFactory);
        TariffController tariffController = new TariffController(tariffFacade, tariffQueryRepository);
        mvc = MockMvcBuilders.standaloneSetup(tariffController)
                .setControllerAdvice(new ExceptionAdviceHandler())
                .build();
    }

}