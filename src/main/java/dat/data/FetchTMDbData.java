package dat.data;

import dat.dao.JSONMovieDAO;
import dat.dao.MovieDAO;
import dat.entities.Movie;
import dat.service.MovieService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchTMDbData {
    static String apiKey = System.getenv("STRING_API_KEY");

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper to parse JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
      //  MovieDAO movieDAO = new JSONMovieDAO(new ObjectMapper());
       // MovieService movieService = new MovieService(movieDAO);

        System.out.println(apiKey);

        List<Movie> moviesList = new ArrayList<>(); // List to collect movies

        // Prepare the initial API request to discover movies
        for (int page = 1; page < 2; page++) {
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

                    for (JsonNode movieNode : movies) {
                        String movieId = movieNode.get("id").asText();
                        String movieTitle = movieNode.get("title").asText();
                        String movieReleaseDate = movieNode.get("release_date").asText();
                        String movieOverview = movieNode.get("overview").asText();
                        //String moviePoster = movieNode.get("poster_path").asText(); //Til billeder xD

                        System.out.println("Movie: " + movieTitle);

                        // Create Movie object
                        Movie movie = objectMapper.treeToValue(movieNode, Movie.class);

                        // Fetch and add more movie details if necessary
                        fetchMovieDetails(client, objectMapper, movieId, movie);
                        fetchMovieCredits(client, objectMapper, movieId, movie);

                        // Add movie to the list
                        moviesList.add(movie);
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                }
            } catch (Exception e) {
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

    private static void fetchMovieDetails(OkHttpClient client, ObjectMapper objectMapper, String movieId, Movie movie) throws Exception {
        Request movieRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieId)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("accept", "application/json")
                .build();

        try (Response movieResponse = client.newCall(movieRequest).execute()) {
            if (movieResponse.isSuccessful()) {
                String jsonResponse = movieResponse.body().string();
                JsonNode movieDetails = objectMapper.readTree(jsonResponse);
                System.out.println("Details: " + movieDetails.toPrettyString());

                // Extract the "genres" array
                JsonNode genresArray = movieDetails.get("genres");
                if (genresArray != null && genresArray.isArray()) {
                    System.out.println("Genres:");
                    for (JsonNode genre : genresArray) {
                        // Grab the "name" field
                        String genreName = genre.get("name").asText();
                        System.out.println(" - " + genreName);
                        // Optionally, add the genreName to the movie object if needed
                    }
                }

                // Update movie object with other details if needed
                // Example: movie.setDescription(movieDetails.get("overview").asText());
            } else {
                System.out.println("Failed to fetch movie details for ID " + movieId + ": " + movieResponse.code());
            }
        }
    }


    // Fetch credits (actors and crew) for a movie and update the Movie object
    private static void fetchMovieCredits(OkHttpClient client, ObjectMapper objectMapper, String movieId, Movie movie) throws Exception {
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
                JsonNode cast = creditsDetails.get("cast");
                System.out.println("Actors:");
                for (JsonNode actor : cast) {
                    System.out.println(" - " + actor.get("name").asText() + " as " + actor.get("character").asText());
                    // Update movie object with actors if needed
                }

                // Print crew information
                JsonNode crew = creditsDetails.get("crew");
                System.out.println("Crew:");
                for (JsonNode crewMember : crew) {
                    System.out.println(" - " + crewMember.get("name").asText() + " (" + crewMember.get("job").asText() + ")");
                    // Update movie object with crew information if needed
                }
            } else {
                System.out.println("Failed to fetch credits for movie ID " + movieId + ": " + creditsResponse.code());
            }
        }
    }
}
