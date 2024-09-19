package dat.data;

import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MovieRepository {
    private EntityManager entityManager;

    public MovieRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Movie movie) {
        try {
            entityManager.getTransaction().begin();

            // Use merge to handle detached entities
            entityManager.merge(movie);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    public static void main(String[] args) {
        // Initialize EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MovieDB");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Initialize MovieRepository
        MovieRepository movieRepository = new MovieRepository(entityManager);

        // Example movie object
        Movie movie = new Movie();
        movie.setTitle("Example Movie");
        // Add other movie properties

        // Save the movie
        movieRepository.save(movie);

        // Close the repository
        movieRepository.close();

        // Close the entity manager factory
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
