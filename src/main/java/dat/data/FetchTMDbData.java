package dat.data;

import dat.entities.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchTMDbData {
    static String apiKey = System.getenv("STRING_API_KEY");
    static Map<Integer, String> genreMap = new HashMap<>();

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper to parse JSON

        // Fetch and store genres in the genreMap
        fetchGenreMap(client, objectMapper);

        List<Movie> movieList = new ArrayList<>(); // List to store movie entities

        // Loop through pages to fetch data
        for (int page = 1; page < 49; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=" + page + "&primary_release_date.gte=2019-01-01&primary_release_date.lte=2024-12-31&region=DK&sort_by=primary_release_date.desc&with_origin_country=DK&with_original_language=da")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey) // Replace with your actual API key
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
                        printActorsAndDirector(client, objectMapper, movie.getId().toString());
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Optionally, print the total number of movies fetched
        System.out.println("Total Movies Fetched: " + movieList.size());
    }

    // Fetch and store genres in a map
    private static void fetchGenreMap(OkHttpClient client, ObjectMapper objectMapper) {
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

    // Fetch and print genres using the genre map
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

    // Fetch detailed information about a movie and print actors and director
    private static void printActorsAndDirector(OkHttpClient client, ObjectMapper objectMapper, String movieId) throws Exception {
        fetchMovieCredits(client, objectMapper, movieId);
    }

    // Fetch credits (actors and crew) for a movie
    private static void fetchMovieCredits(OkHttpClient client, ObjectMapper objectMapper, String movieId) throws Exception {
        Request creditsRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieId + "/credits")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response creditsResponse = client.newCall(creditsRequest).execute()) {
            if (creditsResponse.isSuccessful()) {
                String jsonResponse = creditsResponse.body().string();
                JsonNode creditsDetails = objectMapper.readTree(jsonResponse);

                // Print cast (actors) information
                System.out.println("Actors:");
                JsonNode cast = creditsDetails.get("cast");
                for (JsonNode actor : cast) {
                    String actorName = actor.get("name").asText();
                    String character = actor.get("character").asText();
                    String actorId = actor.get("id").asText(); // Get actor ID
                    String birthdate = fetchPersonDetails(client, objectMapper, actorId); // Fetch birthdate
                    System.out.println(" - " + actorName + " as " + character + " (Born: " + birthdate + ")");
                }

                // Print director information
                System.out.print("Director: ");
                boolean directorFound = false;
                JsonNode crew = creditsDetails.get("crew");
                for (JsonNode crewMember : crew) {
                    if ("Director".equals(crewMember.get("job").asText())) {
                        String directorName = crewMember.get("name").asText();
                        String directorId = crewMember.get("id").asText(); // Get director ID
                        String birthdate = fetchPersonDetails(client, objectMapper, directorId); // Fetch birthdate
                        System.out.println(directorName + " (Born: " + birthdate + ")");
                        directorFound = true;
                        break; // only one director
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

    // Fetch person details (for birthdate)
    private static String fetchPersonDetails(OkHttpClient client, ObjectMapper objectMapper, String personId) throws Exception {
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
        return "null"; // Return unknown if birthdate not found
    }
}
