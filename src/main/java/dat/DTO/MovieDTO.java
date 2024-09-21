package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {

    private long id;
    private String title;
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("original_language")
    private String originalLanguage;

    private List<GenreDTO> genres = new ArrayList<>();
    private List<ActorDTO> actors = new ArrayList<>();
    private DirectorDTO director;
    private Double popularity = 0.0;

    public void setAdult(boolean adult) {
        // Implement if needed
    }

    public List<GenreDTO> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public List<ActorDTO> getActors() {
        return actors != null ? actors : new ArrayList<>();
    }

    public void setActors(List<ActorDTO> actors) {
        this.actors = actors != null ? actors : new ArrayList<>();
    }
}