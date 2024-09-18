package dat.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private String originalLanguage;
    private String overview;
    private List<GenreDTO> genres;
    private List<ActorDTO> actors;
    private Long directorId;
}
