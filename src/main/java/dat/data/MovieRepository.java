package dat.data;

import dat.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class MovieRepository {
    private EntityManager entityManager;

    public MovieRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Method to save a movie entity
    public void save(Movie movie) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(movie); // Merge to handle detached entities
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Method to retrieve all movies from the database
    public List<Movie> getAllMovies() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    // Method to find a movie by its ID
    public Movie findMovieById(Long id) {
        return entityManager.find(Movie.class, id);
    }

    // Method to delete a movie by its ID
    public void deleteMovieById(Long id) {
        try {
            entityManager.getTransaction().begin();
            Movie movie = entityManager.find(Movie.class, id);
            if (movie != null) {
                entityManager.remove(movie);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Close the entity manager
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    public static void main(String[] args) {
        // Initialize EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("moviedb");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Initialize MovieRepository
        MovieRepository movieRepository = new MovieRepository(entityManager);

        // Example movie object
        Movie movie = new Movie();
        movie.setTitle("Example Movie");

        // Save the movie
        movieRepository.save(movie);

        // Retrieve all movies
        List<Movie> movies = movieRepository.getAllMovies();
        System.out.println("Movies in DB:");
        for (Movie m : movies) {
            System.out.println(m.getTitle());
        }

        // Close the repository and entity manager factory
        movieRepository.close();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
