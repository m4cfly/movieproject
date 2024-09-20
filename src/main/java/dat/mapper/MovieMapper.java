package dat.mapper;

import dat.DTO.MovieDTO;
import dat.entities.Movie;
import dat.entities.Actor;
import dat.entities.Director;
import dat.entities.Genre;
import java.util.stream.Collectors;

public class MovieMapper {

    public static Movie toEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setOverview(movieDTO.getOverview());

        // Set the genres
        movie.setGenres(movieDTO.getGenres().stream()
                .map(genreDTO -> new Genre((int) genreDTO.getId().longValue(), genreDTO.getName(), genreDTO.getDescription()))
                .collect(Collectors.toSet()));


        // Set the actors
        movie.setActors(movieDTO.getActors().stream()
                .map(actorDTO -> new Actor(actorDTO.getId(), actorDTO.getName(), actorDTO.getBirthDate().getYear()))
                .collect(Collectors.toSet()));

        // Set the director
        movie.setDirector(new Director(movieDTO.getDirectorId(), "", 0)); // Fill in director data properly

        return movie;
    }
}
