package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.DirectorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "director")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Director {

    @Id
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    private String job;

    public Director(DirectorDTO directorDTO) {
        this.id = directorDTO.getId();
        this.name = directorDTO.getName();
        this.birthdate = directorDTO.getBirthDate();
        this.job = directorDTO.getJob();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Director)) return false;
        Director director = (Director) o;
        return Objects.equals(getId(), director.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}