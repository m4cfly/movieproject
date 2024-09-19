package dat.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.entities.Movie;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchTMDbData {

    static String apiKey = System.getenv("STRING_API_KEY");
    static Map<Integer, String> genreMap = new HashMap<>();
    static ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper

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

        // Fetch and store genres in the genreMap
        fetchGenreMap(client);

        // Loop through pages to fetch data
        for (int page = 1; page < 2; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=" + page + "&primary_release_date.gte=2019-01-01&primary_release_date.lte=2024-12-31&region=DK&sort_by=primary_release_date.desc&with_origin_country=DK&with_original_language=da")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            try {
                // Execute the request and get the response
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    // Parse the JSON response
                    JsonNode rootNode = objectMapper.readTree(jsonResponse);
                    JsonNode movies = rootNode.get("results");

                    // Loop through each movie node and convert to Movie entity
                    for (JsonNode movieNode : movies) {
                        Movie movie = objectMapper.treeToValue(movieNode, Movie.class); // Convert JSON object to Movie
                        movieList.add(movie);

                        // Print Movie information
                        System.out.println("\nMovie: " + movie.getTitle());
                        printGenres(movieNode);
                        printActorsAndDirector(client, movie.getId().toString());
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMoviesToFile(List<Movie> movieList, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), movieList);
            System.out.println("Movies saved to file: " + filePath);
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

    // Fetch and store genres in a map
    private static void fetchGenreMap(OkHttpClient client) {
        Request genreRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/genre/movie/list?language=en-US")
                .get()
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response genreResponse = client.newCall(genreRequest).execute()) {
            if (genreResponse.isSuccessful()) {
                String jsonResponse = genreResponse.body().string();
                JsonNode genreRoot = objectMapper.readTree(jsonResponse);
                JsonNode genres = genreRoot.get("genres");

                for (JsonNode genre : genres) {
                    int id = genre.get("id").asInt();
                    String name = genre.get("name").asText();
                    genreMap.put(id, name);  // Store id-name mapping in the map
                }
            } else {
                System.out.println("Failed to fetch genres: " + genreResponse.code() + " - " + genreResponse.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printGenres(JsonNode movieNode) {
        System.out.print("Genres: ");
        JsonNode genres = movieNode.get("genre_ids");
        if (genres != null && genres.isArray()) {
            for (JsonNode genre : genres) {
                int genreId = genre.asInt();
                String genreName = genreMap.get(genreId);
                if (genreName != null) {
                    System.out.print(genreName + " ");
                }
            }
        }
        System.out.println();
    }

    private static void printActorsAndDirector(OkHttpClient client, String movieId) throws Exception {
        fetchMovieCredits(client, movieId);
    }

    private static void fetchMovieCredits(OkHttpClient client, String movieId) throws Exception {
        Request creditsRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieId + "/credits")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response creditsResponse = client.newCall(creditsRequest).execute()) {
            if (creditsResponse.isSuccessful()) {
                String jsonResponse = creditsResponse.body().string();
                JsonNode creditsDetails = objectMapper.readTree(jsonResponse);

                System.out.println("Actors:");
                JsonNode cast = creditsDetails.get("cast");
                for (JsonNode actor : cast) {
                    String actorName = actor.get("name").asText();
                    String character = actor.get("character").asText();
                    String actorId = actor.get("id").asText(); // Get actor ID
                    String birthdate = fetchPersonDetails(client, actorId); // Fetch birthdate
                    System.out.println(" - " + actorName + " as " + character + " (Born: " + birthdate + ")");
                }

                System.out.print("Director: ");
                boolean directorFound = false;
                JsonNode crew = creditsDetails.get("crew");
                for (JsonNode crewMember : crew) {
                    if ("Director".equals(crewMember.get("job").asText())) {
                        String directorName = crewMember.get("name").asText();
                        String directorId = crewMember.get("id").asText(); // Get director ID
                        String birthdate = fetchPersonDetails(client, directorId); // Fetch birthdate
                        System.out.println(directorName + " (Born: " + birthdate + ")");
                        directorFound = true;
                        break; // Assume only one director
                    }
                }
                if (!directorFound) {
                    System.out.println("Unknown");
                }
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
