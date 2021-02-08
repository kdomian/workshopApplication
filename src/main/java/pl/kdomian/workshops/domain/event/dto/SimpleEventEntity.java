package pl.kdomian.workshops.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "EVENT")
@Getter
@NoArgsConstructor
public class SimpleEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SimpleEventEntity(Long id) {
        this.id = id;
    }
}
