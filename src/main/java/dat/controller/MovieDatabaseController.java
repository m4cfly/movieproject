package dat.controller;

import dat.data.FetchTMDbData;
import dat.entities.Movie;
import dat.dao.JPAMovieDAO;

import java.util.List;
import java.util.Scanner;

public class MovieDatabaseController {

    private JPAMovieDAO movieDAO;

    public MovieDatabaseController(JPAMovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // Menu-driven controller to handle different user actions
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMovie Database Controller:");
            System.out.println("1. Fetch Movies from API");
            System.out.println("2. Save Fetched Movies to Database");
            System.out.println("3. Show All Movies from Database");
            System.out.println("4. Find a Movie by Title");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = getUserInput(scanner);

            switch (choice) {
                case 1 -> fetchMoviesFromAPI();
                case 2 -> saveMoviesToDatabase();
                case 3 -> showAllMovies();
                case 5 -> exit();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getUserInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private void fetchMoviesFromAPI() {
        System.out.println("Fetching movies from API...");
        FetchTMDbData.fetchMovieData(FetchTMDbData.readMoviesFromFile("movies.json"));
        System.out.println("Movies fetched successfully.");
    }

    private void saveMoviesToDatabase() {
        System.out.println("Saving fetched movies to database...");
        List<Movie> movieList = FetchTMDbData.readMoviesFromFile("movies.json");
        if (movieList != null && !movieList.isEmpty()) {
            FetchTMDbData.saveMoviesToDatabase(movieList);
            System.out.println("Movies saved to the database.");
        } else {
            System.out.println("No movies to save. Please fetch movies from the API first.");
        }
    }

    private void showAllMovies() {
        System.out.println("Showing all movies from the database...");
        List<Movie> movieList = movieDAO.getAllMovies();
        if (movieList != null && !movieList.isEmpty()) {
            for (Movie movie : movieList) {
                System.out.println(movie);
            }
        } else {
            System.out.println("No movies found in the database.");
        }
    }


    private void exit() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }
}
