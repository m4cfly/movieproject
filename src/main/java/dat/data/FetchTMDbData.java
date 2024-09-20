package dat.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.entities.Movie;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import dat.DTO.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchTMDbData {
    static String apiKey = System.getenv("STRING_API_KEY");
    static ObjectMapper objectMapper = new ObjectMapper();
    static {
        // Configure the ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule()); // Handles LocalDate deserialization
    }
    public static void main(String[] args) throws Exception {

    static {
        // Configure the ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule()); // Handles LocalDate deserialization
    }

    public static void main(String[] args) {
        String filePath = "movies.json";

        // Load movies from file if they exist
        List<Movie> movieList = readMoviesFromFile(filePath);
        if (movieList == null || movieList.isEmpty()) {
            movieList = new ArrayList<>();

            // Fetch movies from API
            fetchMovieData(movieList);

            // Save movies to file
            writeMoviesToFile(movieList, filePath);
        } else {
            System.out.println("Movies loaded from file: " + filePath);
        }

        // Optionally save movies to database
        saveMoviesToDatabase(movieList);
    }

    public static void fetchMovieData(List<Movie> movieList) {
        OkHttpClient client = new OkHttpClient();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);  // Enable pretty-printing

        List<MovieDTO> moviesList = new ArrayList<>();  // List to store movies

        int test = 0;

        // Discover movies (API request)
        for (int page = 1; page <= 49; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=" + page +
                            "&primary_release_date.gte=2019-01-01&primary_release_date.lte=2024-12-31&region=DK&sort_by=primary_release_date.desc&with_origin_country=DK&with_original_language=da")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    // Parse the JSON response
                    JsonNode rootNode = objectMapper.readTree(jsonResponse);
                    JsonNode movies = rootNode.get("results");

                    for (JsonNode movieNode : movies) {
                        MovieDTO movie = objectMapper.treeToValue(movieNode, MovieDTO.class);

                        String movieId = movieNode.get("id").asText();

                        // Fetch and update movie details and credits
                        fetchMovieDetails(client, objectMapper, movieId, movie);
                        fetchMovieCredits(client, objectMapper, movieId, movie);

                        test++;
                        // Print the movie details (optional)
                        System.out.println(movie + "\n" + String.valueOf(test) + "\n");


                        // Add movie to list
                        moviesList.add(movie);
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write the movie list to a JSON file
        try {
            objectMapper.writeValue(new File("movies.json"), moviesList);
            System.out.println("Movies data has been written to movies.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static List<Movie> readMoviesFromFile(String filePath) {
            try {
                return objectMapper.readValue(new File(filePath), new TypeReference<List<Movie>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void saveMoviesToDatabase(List<Movie> movieList) {
            EntityManager entityManager = HibernateConfig.getEntityManagerFactory("MovieDB").createEntityManager();
            MovieRepository movieRepository = new MovieRepository(entityManager);

            for (Movie movie : movieList) {
                movieRepository.save(movie);
            }

            movieRepository.close();
        }

    // Fetch detailed information about the movie
    private static void fetchMovieDetails(OkHttpClient client, ObjectMapper objectMapper, String movieId, MovieDTO movie) throws IOException {
        Request movieRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieId)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response movieResponse = client.newCall(movieRequest).execute()) {
            if (movieResponse.isSuccessful()) {
                String jsonResponse = movieResponse.body().string();
                JsonNode movieDetails = objectMapper.readTree(jsonResponse);

                // Parse and populate genres
                List<GenreDTO> genres = new ArrayList<>();
                JsonNode genresArray = movieDetails.get("genres");
                if (genresArray != null && genresArray.isArray()) {
                    for (JsonNode genreNode : genresArray) {
                        GenreDTO genre = objectMapper.treeToValue(genreNode, GenreDTO.class);
                        genres.add(genre);
                    }
                }
                movie.setGenres(genres);

                // Update other movie details (e.g., overview, runtime, release_date)
                movie.setOverview(movieDetails.get("overview").asText());
                movie.setReleaseDate(movieDetails.get("release_date").asText());
            } else {
                System.out.println("Failed to fetch movie details for ID " + movieId + ": " + movieResponse.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch credits (actors and crew) for a movie
    private static void fetchMovieCredits(OkHttpClient client, ObjectMapper objectMapper, String movieId, MovieDTO movie) throws Exception {
        Request creditsRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieId + "/credits")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response creditsResponse = client.newCall(creditsRequest).execute()) {
            if (creditsResponse.isSuccessful()) {
                String jsonResponse = creditsResponse.body().string();
                JsonNode creditsDetails = objectMapper.readTree(jsonResponse);

                // Parse cast
                List<ActorDTO> actorList = new ArrayList<>();
                JsonNode castArray = creditsDetails.get("cast");
                if (castArray != null && castArray.isArray()) {
                    for (JsonNode castNode : castArray) {
                        ActorDTO cast = objectMapper.treeToValue(castNode, ActorDTO.class);
                        String birthdate = fetchPersonDetails(client, String.valueOf(cast.getId()));
                        cast.setBirthDate(birthdate);;
                        actorList.add(cast);
                    }
                }

                // Parse crew
                List<DirectorDTO> directorList = new ArrayList<>();
                JsonNode crewArray = creditsDetails.get("crew");
                if (crewArray != null && crewArray.isArray()) {
                    for (JsonNode crewNode : crewArray) {
                        if (crewNode.get("job").asText().equals("Director")) {
                            DirectorDTO crew = objectMapper.treeToValue(crewNode, DirectorDTO.class);
                            String birthdate = fetchPersonDetails(client, String.valueOf(crew.getId()));
                            crew.setBirthDate(birthdate);
                            directorList.add(crew);
                        }

                    }
                }

                // Set cast and crew in the movie DTO
                CreditsDTO credits = new CreditsDTO();
                credits.setActors(actorList);
                credits.setDirector(directorList);
                movie.setCredits(credits);
            } else {
                System.out.println("Failed to fetch credits for movie ID " + movieId + ": " + creditsResponse.code());
            }
        }
    }

    private static String fetchPersonDetails(OkHttpClient client, String personId) throws Exception {
        Request personRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/person/" + personId)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response personResponse = client.newCall(personRequest).execute()) {
            if (personResponse.isSuccessful()) {
                String jsonResponse = personResponse.body().string();
                JsonNode personDetails = objectMapper.readTree(jsonResponse);
                return personDetails.get("birthday").asText(); // Fetch the birthdate
            }
        }
        return "Unknown"; // Return unknown if birthdate not found
    }
}

