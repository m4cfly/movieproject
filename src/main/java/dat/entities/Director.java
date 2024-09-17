package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private int birthdate;

    public Long getId() {
        return id;
    }
}
