package dat.dao;

import dat.DTO.MovieDTO;
import java.util.List;

public interface MovieDAO {
    void saveMovies(List<MovieDTO> movies);
    List<MovieDTO> loadMovies();
}
