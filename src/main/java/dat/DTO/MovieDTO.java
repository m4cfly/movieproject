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

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("belongs_to_collection")
    private Object belongsToCollection;  // Assuming it can be null

    private int budget;
    private List<GenreDTO> genres;
    private String homepage;
    private long id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;
    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("production_companies")
    private List<ProductionCompanyDTO> productionCompanies;

    @JsonProperty("production_countries")
    private List<ProductionCountryDTO> productionCountries;

    @JsonProperty("release_date")
    private String releaseDate;

    private long revenue;
    private int runtime;

    @JsonProperty("spoken_languages")
    private List<SpokenLanguageDTO> spokenLanguages;

    private String status;
    private String tagline;
    private String title;
    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    private CreditsDTO credits;  // Nested DTO for credits (cast and crew)
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class GenreDTO {
    private long id;
    private String name;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductionCompanyDTO {
    private long id;

    @JsonProperty("logo_path")
    private String logoPath;

    private String name;

    @JsonProperty("origin_country")
    private String originCountry;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductionCountryDTO {
    @JsonProperty("iso_3166_1")
    private String iso31661;

    private String name;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class SpokenLanguageDTO {
    @JsonProperty("english_name")
    private String englishName;

    @JsonProperty("iso_639_1")
    private String iso6391;

    private String name;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditsDTO {
    private List<CastDTO> cast;
    private List<CrewDTO> crew;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class CastDTO {
    private boolean adult;
    private int gender;
    private long id;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("cast_id")
    private int castId;

    private String character;

    @JsonProperty("credit_id")
    private String creditId;

    private int order;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class CrewDTO {
    private boolean adult;
    private int gender;
    private long id;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("credit_id")
    private String creditId;

    private String department;
    private String job;
}
