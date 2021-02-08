package pl.kdomian.workshops.domain.tariff;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kdomian.workshops.domain.tariff.dto.TariffDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tariff")
@RequiredArgsConstructor
class TariffController {

    private final TariffFacade tariffFacade;

    @GetMapping("/")
    ResponseEntity<List<TariffDTO>> getTariffs() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tariffFacade.getTariffs());
    }

    @GetMapping("/{id}")
    ResponseEntity<TariffDTO> getTariff(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tariffFacade.getTariff(id));
    }

    @PostMapping("/")
    ResponseEntity<TariffDTO> createTariff(@Valid @RequestBody TariffDTO tariffDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tariffFacade.createTariff(tariffDTO));
    }


}
