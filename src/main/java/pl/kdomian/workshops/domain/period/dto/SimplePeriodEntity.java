package pl.kdomian.workshops.domain.period.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PERIOD")
@Getter
@NoArgsConstructor
public class SimplePeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SimplePeriodEntity(Long id) {
        this.id = id;
    }
}
