package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.ActorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor {

    @Id
    private Integer id;

    private String name;

    @JsonProperty("birth_date")
    private String birthDate;

    private String job;

    private String character;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Actor(ActorDTO actorDTO) {
        this.id = actorDTO.getId();
        this.name = actorDTO.getName();
        this.character = actorDTO.getCharacter();
        this.birthDate = actorDTO.getBirthDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return Objects.equals(getId(), actor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}