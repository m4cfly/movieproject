package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.MovieDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(nullable = false)
    private boolean adult = false;

    private String title;

    @JsonProperty("original_language")
    @Column(name = "original_language")
    private String originalLanguage;

    @JsonProperty("release_date")
    @Column(name = "release_date")
    private String releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(nullable = false)
    private Long budget = 0L;

    @Column(nullable = false)
    private Double popularity = 0.0;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor> actors = new ArrayList<>();

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Credits credits;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "director_id")
    private Director director;

    public Movie(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.originalLanguage = movieDTO.getOriginalLanguage();
        this.releaseDate = movieDTO.getReleaseDate();
        this.overview = movieDTO.getOverview();
        this.popularity = movieDTO.getPopularity();

        if (movieDTO.getGenres() != null) {
            this.genres = new ArrayList<>();
            for (dat.DTO.GenreDTO genreDTO : movieDTO.getGenres()) {
                this.genres.add(new Genre(genreDTO));
            }
        }

        if (movieDTO.getActors() != null) {
            this.actors = new ArrayList<>();
            for (dat.DTO.ActorDTO actorDTO : movieDTO.getActors()) {
                Actor actor = new Actor(actorDTO);
                actor.setMovie(this);
                this.actors.add(actor);
            }
        }

        if (movieDTO.getDirector() != null) {
            this.director = new Director(movieDTO.getDirector());
        }
    }
}