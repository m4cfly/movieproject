package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.DirectorDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "director")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate")
    private String birthdate;

    private String job;

    public Director(DirectorDTO directorDTO) {
        this.name = directorDTO.getName();
        this.birthdate = directorDTO.getBirthDate();
        this.job = directorDTO.getJob();
    }
}