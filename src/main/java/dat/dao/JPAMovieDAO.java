package dat.dao;

import dat.DTO.MovieDTO;
import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class JPAMovieDAO implements MovieDAO {
    private static EntityManagerFactory entityManagerFactory;

    private static JPAMovieDAO instance;



    public static JPAMovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            entityManagerFactory = emf;
            instance = new JPAMovieDAO();
        }
        return instance;

    }

    @Override
    public void saveMovies(Movie movie) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(movie);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Movie> loadMovies() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Movie> movies = entityManager.createQuery("from Movie").getResultList();
        entityManager.getTransaction().commit();
        return movies;
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
