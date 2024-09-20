package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.DTO.MovieDTO;
import jakarta.persistence.*;
import dat.DTO.GenreDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private int id;

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
    private String releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private double popularity;
    @JsonProperty("vote_average") // Ensures JSON field maps correctly to this field
    @Column(name = "vote_average")
    private double voteAverage;

    @ManyToMany (cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;


    private int revenue;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Credits credits;

    private String tagline;


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


    public Movie(MovieDTO movieDTO) {
        this.id = movieDTO.getId();
        this.adult = movieDTO.isAdult();
        this.title = movieDTO.getTitle();
        this.budget = movieDTO.getBudget();
        this.imdbId = movieDTO.getImdbId();
        this.originalLanguage = movieDTO.getOriginalLanguage();
        this.originalTitle = movieDTO.getOriginalTitle();
        this.releaseDate = movieDTO.getReleaseDate();
        this.overview = movieDTO.getOverview();
        this.popularity = movieDTO.getPopularity();
        this.voteAverage = movieDTO.getVoteAverage();
        this.revenue = movieDTO.getRevenue();
        this.tagline = movieDTO.getTagline();
        this.voteCount = movieDTO.getVoteCount();

        // Convert List<GenreDTO> to List<Genre>
        if (movieDTO.getGenres() != null) {
            this.genres = new ArrayList<>();
            for (GenreDTO genreDTO : movieDTO.getGenres()) {
                Genre genre = new Genre(genreDTO); // Ensures both ID and name are set
                this.genres.add(genre);
            }
        }

        // Convert credits from DTO to entity if necessary
        if (movieDTO.getCredits() != null) {
            this.credits = new Credits(movieDTO.getCredits());
        }

    }

}
