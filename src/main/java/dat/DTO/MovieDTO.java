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
    private boolean adult;

    private int budget;
    private List<GenreDTO> genres;

    private int id;

    @JsonProperty("imdb_id")
    private String imdbId;


    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;
    private double popularity;


    @JsonProperty("release_date")
    private String releaseDate;

    private int revenue;

    private String tagline;
    private String title;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    private CreditsDTO credits;  // Nested DTO for credits (cast and crew)
}

