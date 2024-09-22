package dat.dao;

import dat.entities.Movie;
import dat.entities.Genre;
import dat.entities.Director;
import dat.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class JPAMovieDAO implements MovieDAO {

    private final EntityManagerFactory entityManagerFactory;

    public JPAMovieDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveMovie(Movie movie) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            // Håndter genrer
            List<Genre> managedGenres = new ArrayList<>();
            for (Genre genre : movie.getGenres()) {
                managedGenres.add(em.merge(genre));
            }
            movie.setGenres(managedGenres);

            // Håndter instruktør
            if (movie.getDirector() != null) {
                Director managedDirector = em.merge(movie.getDirector());
                movie.setDirector(managedDirector);
            }

            // Håndter skuespillere
            List<Actor> managedActors = new ArrayList<>();
            for (Actor actor : movie.getActors()) {
                Actor managedActor = em.merge(actor);
//                managedActor.setMovie(movie);
                managedActors.add(managedActor);
            }
            movie.setActors(managedActors);

            // Gem eller opdater filmen
            movie = em.merge(movie);

            em.getTransaction().commit();
            System.out.println("Film gemt med succes: " + movie.getTitle());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Fejl ved gemning af film: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            saveMovie(movie);
        }
    }

    @Override
    public Movie findMovieById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Movie.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        } finally {
            em.close();
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