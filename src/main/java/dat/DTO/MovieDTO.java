package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {

    private int id;
    private String title;
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("original_language")
    private String originalLanguage;

    private List<GenreDTO> genres;
    private List<ActorDTO> actors;
    private DirectorDTO director;
    private Double popularity = 0.0;

    public void setAdult(boolean adult) {
        // Implement if needed
    }
}