package dat.dao;

import dat.DTO.MovieDTO;
import dat.entities.Movie;
import java.util.List;

public interface MovieDAO {
    void saveMovies(Movie movie);

    List<Movie> loadMovies();

    void insertMovie(Movie movie);
    Movie findMovieById(Long id);
    List<Movie> getAllMovies();
}

