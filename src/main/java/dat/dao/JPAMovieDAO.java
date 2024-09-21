package dat.dao;

import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class JPAMovieDAO implements MovieDAO {

    private final EntityManagerFactory entityManagerFactory;

    public JPAMovieDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveMovie(Movie movie) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            if (movie.getDirector() != null && movie.getDirector().getId() == null) {
                entityManager.persist(movie.getDirector());
            }

            if (movie.getId() == null) {
                entityManager.persist(movie);
            } else {
                entityManager.merge(movie);
            }

            entityManager.getTransaction().commit();
            System.out.println("Movie saved successfully: " + movie.getTitle());
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error saving movie: " + e.getMessage());
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            for (Movie movie : movies) {
                if (movie.getId() == null) {
                    entityManager.persist(movie);
                } else {
                    entityManager.merge(movie);
                }
            }
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
    public Movie findMovieById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Movie.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Movie> loadMovies() {
        return getAllMovies();
    }

    @Override
    public void insertMovie(Movie movie) {
        saveMovie(movie);
    }
}