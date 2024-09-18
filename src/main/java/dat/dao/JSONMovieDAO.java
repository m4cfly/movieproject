package dat.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.DTO.MovieDTO;
import dat.dao.MovieDAO;

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
    public void saveMovies(List<MovieDTO> movies) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), movies);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save movies", e);
        }
    }

    @Override
    public List<MovieDTO> loadMovies() {
        try {
            MovieDTO[] movieDTOS = objectMapper.readValue(new File(FILE_PATH), MovieDTO[].class);
            return List.of(movieDTOS);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load movies", e);
        }
    }
}
