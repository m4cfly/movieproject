package dat.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private String title;
    private LocalDate releaseDate;
    private String id; // Add TMDb ID
    private List<ActorDTO> cast; // List of actors
    private List<DirectorDTO> crew; // List of directors
    private String genre;
    private String actor;
    private String director;

    // Add constructor to handle data from the API if necessary
    public MovieDTO(String title, LocalDate releaseDate, String id, String genre, String actor, String director) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.id = id;
        this.genre = genre;
        this.actor = actor;
        this.director = director;
    }

    // Copy constructor that maps DTO fields from another MovieDTO instance
    public MovieDTO(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.releaseDate = movieDTO.getReleaseDate();
        this.id = movieDTO.getId();
        this.genre = movieDTO.getGenre();
        this.actor = movieDTO.getActor();
        this.director = movieDTO.getDirector();

        // Initialize lists with their respective DTOs
        this.cast = movieDTO.getCast().stream()
                .map(actorDTO -> new ActorDTO(actorDTO.getName(), actorDTO.getBirthdate()))
                .collect(Collectors.toList());

        this.crew = movieDTO.getCrew().stream()
                .map(directorDTO -> new DirectorDTO(directorDTO.getName(), directorDTO.getBirthdate()))
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreDTO {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActorDTO {
        private String name;
        private LocalDate birthdate;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectorDTO {
        private String name;
        private LocalDate birthdate;
    }

    // Getter methods
    public String getGenre() {
        return genre;
    }

    public String getActor() {
        return actor;
    }

    public String getDirector() {
        return director;
    }
}
