package dat.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.DTO.MovieDTO;
import dat.entities.Movie;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONMovieDAO implements MovieDAO {

    private static final String FILE_PATH = "movieInfo.json";
    private final ObjectMapper objectMapper;

    public JSONMovieDAO(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveMovies(Movie movie) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), movie);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save movies", e);
        }
    }

    @Override
    public List<Movie> loadMovies() {
        try {
            Movie[] movieS = objectMapper.readValue(new File(FILE_PATH), Movie[].class);
            return List.of(movieS);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load movies", e);
        }
    }

    @Override
    public void insertMovie(Movie movie) {

    }

    @Override
    public Movie findMovieById(Long id) {
        return null;
    }

    @Override
    public List<Movie> getAllMovies() {
        return List.of();
    }
}
