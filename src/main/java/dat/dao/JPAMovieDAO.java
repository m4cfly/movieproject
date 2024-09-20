package dat.dao;

import dat.DTO.MovieDTO;
import dat.entities.Genre;
import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
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
//        getInstance(entityManagerFactory);
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
//        entityManager.merge(movie);
//        entityManager.getTransaction().commit();
        getInstance(entityManagerFactory);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Check for each genre in the movie before merging
            List<Genre> existingGenres = new ArrayList<>();
            for (Genre genre : movie.getGenres()) {
                Genre existingGenre = entityManager.find(Genre.class, genre.getId());
                if (existingGenre != null) {
                    existingGenres.add(existingGenre);  // Add the existing genre
                } else {
                    entityManager.persist(genre);  // Persist the genre if not found
                    existingGenres.add(genre);
                }
            }
            movie.setGenres(existingGenres);  // Set the existing or newly added genres

            entityManager.merge(movie.getCredits().getActors());
            entityManager.getTransaction().commit();
            entityManager.merge(movie.getCredits().getDirector());
            entityManager.getTransaction().commit();
            entityManager.merge(movie.getCredits());
            entityManager.getTransaction().commit();
            entityManager.merge(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Movie> loadMovies() {
        getInstance(entityManagerFactory);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Movie> movies = entityManager.createQuery("from Movie").getResultList();
        entityManager.getTransaction().commit();
        return movies;
    }

    @Override
    public void insertMovie(Movie movie) {
        getInstance(entityManagerFactory);
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
        getInstance(entityManagerFactory);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Movie.class, id); // Find a movie by ID
        } finally {
            entityManager.close(); // Close the EntityManager
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        getInstance(entityManagerFactory);
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
