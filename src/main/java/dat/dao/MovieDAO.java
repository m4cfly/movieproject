package dat.dao;

import dat.entities.Movie;
import java.util.List;

public interface MovieDAO {

    void saveMovie(Movie movie); // Save or update a single movie

    void saveMovies(List<Movie> movies); // Save or update a list of movies

    Movie findMovieById(Long id); // Find a movie by its ID

    List<Movie> getAllMovies(); // Get a list of all movies

    List<Movie> loadMovies();

    void insertMovie(Movie movie); // Insert a new movie
}
