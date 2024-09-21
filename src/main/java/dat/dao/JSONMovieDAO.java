package dat.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.entities.Movie;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONMovieDAO implements MovieDAO {

    private static final String FILE_PATH = "movieInfo.json";
    private final ObjectMapper objectMapper;

    public JSONMovieDAO(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveMovie(Movie movie) {  // Updated to match the interface method
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), movie);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save movie", e);
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {

    }

    @Override
    public List<Movie> loadMovies() {
        try {
            Movie[] movies = objectMapper.readValue(new File(FILE_PATH), Movie[].class);
            return List.of(movies);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load movies", e);
        }
    }

    @Override
    public void insertMovie(Movie movie) {
        // Logic for inserting a new movie into the JSON file (if needed)
    }

    @Override
    public Movie findMovieById(Long id) {
        // Logic for finding a movie by ID in the JSON file
        return null;
    }

    @Override
    public List<Movie> getAllMovies() {
        // Logic for retrieving all movies from the JSON file
        return List.of();
    }
}
