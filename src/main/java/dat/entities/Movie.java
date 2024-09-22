package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.MovieDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    @Column(name = "id")
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

    @Column()
    private Long budget;

    @Column(nullable = false)
    private Double popularity;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor> actors = new ArrayList<>();

//    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Credits credits;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "director_id")
    private Director director;

    public Movie(MovieDTO movieDTO) {
        this.id = movieDTO.getId();
        this.title = movieDTO.getTitle();
        this.originalLanguage = movieDTO.getOriginalLanguage();
        this.releaseDate = movieDTO.getReleaseDate();
        this.overview = movieDTO.getOverview();
        this.popularity = movieDTO.getPopularity();

        this.genres = movieDTO.getGenres().stream()
                .map(Genre::new)
                .collect(Collectors.toList());

        this.actors = movieDTO.getActors().stream()
                .map(actorDTO -> {
                    Actor actor = new Actor(actorDTO);
                    actor.setMovie(this);
                    return actor;
                })
                .collect(Collectors.toList());

        if (movieDTO.getDirector() != null) {
            this.director = new Director(movieDTO.getDirector());
        }
    }

    public List<Genre> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public List<Actor> getActors() {
        return actors != null ? actors : new ArrayList<>();
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors != null ? actors : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}