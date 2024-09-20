package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.DirectorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)

public class Director {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private String birthdate;

    private String job;

    public Director(DirectorDTO directorDTO) {
        this.id = directorDTO.getId();
        this.name = directorDTO.getName();
        this.birthdate = directorDTO.getBirthDate();
        this.job = directorDTO.getJob();
    }

    public int getId() {
        return id;
    }
}

