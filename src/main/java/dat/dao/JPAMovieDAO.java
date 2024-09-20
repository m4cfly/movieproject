package dat.dao;

import dat.DTO.MovieDTO;
import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class JPAMovieDAO implements MovieDAO {
    private EntityManagerFactory entityManagerFactory;

    public JPAMovieDAO() {
        // Create an EntityManagerFactory with the persistence unit name
        this.entityManagerFactory = Persistence.createEntityManagerFactory("moviePU");
    }

    @Override
    public void saveMovies(List<MovieDTO> movies) {

    }

    @Override
    public List<MovieDTO> loadMovies() {
        return List.of();
    }

    @Override
    public void insertMovie(Movie movie) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin(); // Begin transaction
            entityManager.persist(movie); // Persist the movie entity
            entityManager.getTransaction().commit(); // Commit the transaction
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if there's an error
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Close the EntityManager
        }
    }

    @Override
    public Movie findMovieById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Movie.class, id); // Find a movie by ID
        } finally {
            entityManager.close(); // Close the EntityManager
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList(); // Get all movies
        } finally {
            entityManager.close(); // Close the EntityManager
        }
    }

    // Clean up resources
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
