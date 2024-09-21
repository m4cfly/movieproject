package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.ActorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonProperty("birth_date")
    private String birthDate;

    private String job;

    private String character;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Actor(ActorDTO actorDTO) {
        this.name = actorDTO.getName();
        this.birthDate = actorDTO.getBirthDate();
        this.job = actorDTO.getJob();
        this.character = actorDTO.getCharacter();
    }
}