package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.ActorDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor {
    @Id
    private int id;
    @JsonProperty ("birth_date")
    private String birthDate;
    private String name;
    @JsonProperty("cast_id")
    private int actorId;
    private String character;

    public Actor(ActorDTO actorDTO) {
        this.id = actorDTO.getId();
        this.birthDate = actorDTO.getBirthDate();
        this.name = actorDTO.getName();
        this.actorId = actorDTO.getActorId();
        this.character = actorDTO.getCharacter();

    }
}
