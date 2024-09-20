package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.CreditsDTO;
import dat.DTO.GenreDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies") // Specifies the table name
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean adult;

    private String title;

    private int budget;

    @JsonProperty("imdb_id")
    private String imdbId;


    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date") // Ensures JSON field maps correctly to this field
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private double popularity;
    @JsonProperty("vote_average") // Ensures JSON field maps correctly to this field
    @Column(name = "vote_average")
    private double voteAverage;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;


    private long revenue;

    @OneToOne(cascade = CascadeType.ALL)
    private Credits credits;

    private String tagline;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @JsonProperty("vote_count")
    private int voteCount;


    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return this.credits.getActors();
    }

    public void setActors(List<Actor> actors) {
         this.credits.setActors(actors);
    }

    public Director getDirector() {
        return this.credits.getDirector();
    }

    public void setDirector(Director director) {
        this.credits.setDirector(director);
    }
}
