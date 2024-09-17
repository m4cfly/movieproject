package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty ("id")
    private Long id;

    @JsonProperty ("title")
    private String title;

    @Column(name = "release_date")
    @JsonProperty ("release_date")
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    @JsonProperty ("overview")
    private String overview;

    @ManyToMany (cascade = CascadeType.ALL)
    @JsonProperty ("genres")
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany (cascade = CascadeType.ALL)
    @JsonProperty ("Actors")
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @ManyToOne (cascade = CascadeType.ALL)
    @JsonProperty ("job : director") // problem med at director-titlen ligger indenfor job, og at job også har andre titler vi ikke vil have
    @JoinColumn(name = "director_id")
    private Director director;
}
