package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.ActorDTO;
import dat.DTO.CreditsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "credits")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Credits(CreditsDTO creditsDTO) {
        if (creditsDTO.getActors() != null) {
            for (ActorDTO actorDTO : creditsDTO.getActors()) {
                this.actors.add(new Actor(actorDTO));
            }
        }

        if (creditsDTO.getDirector() != null) {
            this.director = new Director(creditsDTO.getDirector());
        }
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void removeActor(Actor actor) {
        this.actors.remove(actor);
    }
}