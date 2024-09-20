package dat.services;

import dat.DTO.MovieDTO;
import dat.entities.Movie;
import dat.data.MovieRepository;
import dat.mapper.MovieMapper;
import jakarta.persistence.EntityManager;

public class MovieService {
    private MovieRepository movieRepository;

    public MovieService(EntityManager entityManager) {
        this.movieRepository = new MovieRepository(entityManager);
    }

    public void fetchAndSaveMovie(MovieDTO movieDTO) {
        Movie movie = MovieMapper.toEntity(movieDTO);
        movieRepository.save(movie);
    }

    public void close() {
        movieRepository.close();
    }
}
