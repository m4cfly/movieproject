package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.ActorDTO;
import dat.DTO.CreditsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;


    public Credits(CreditsDTO creditsDTO) {
        // Convert actors list from DTO to entity
        if (creditsDTO.getActors() != null) {
            this.actors = new ArrayList<>();
            for (ActorDTO actorDTO : creditsDTO.getActors()) {
                this.actors.add(new Actor(actorDTO));  // Assuming Actor has a constructor that accepts CastDTO
            }
        }

        // Convert director from DTO to entity
        if (creditsDTO.getDirector() != null) {
            this.director = new Director(creditsDTO.getDirector());  // Assuming Director has a constructor that accepts DirectorDTO
        }
    }

}
