package dat.data;

import dat.entities.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FetchTMDbData {
        static String apiKey = System.getenv("STRING_API_KEY");
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper to parse JSON

//        System.out.println(apiKey);

        // Prepare the initial API request to discover movies

            Request request = null;
            int test = 0;

            for (int page = 1; page < 49; page++) {
                request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=" + page + "&primary_release_date.gte=2019-01-01&primary_release_date.lte=2024-12-31&region=DK&sort_by=primary_release_date.desc&with_origin_country=DK&with_original_language=da")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + apiKey) // Replace with your actual API key
                        .build();try {

                            // Execute the request and get the response
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();

                        // Parse the JSON response
                        JsonNode rootNode = objectMapper.readTree(jsonResponse);
                        JsonNode movies = rootNode.get("results");

                        for (JsonNode movie : movies) {
                            String movieId = movie.get("id").asText();
                            String movieTitle = movie.get("title").asText();

                            System.out.println("Movie: " + movieTitle);

                            // Fetch and print movie details
                            fetchMovieDetails(client, objectMapper, movieId);

                            // Fetch and print movie credits
                            fetchMovieCredits(client, objectMapper, movieId);
                            test++;
                            objectMapper.readValue(jsonResponse, Movie.class); // prøver om vi evt. kan lave om til Movie objekter?
                        }
                    } else {
                        System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        System.out.println(test);
            }



    // Fetch detailed information about a movie
    private static void fetchMovieDetails(OkHttpClient client, ObjectMapper objectMapper, String movieId) throws Exception {
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
            } else {
                System.out.println("Failed to fetch movie details for ID " + movieId + ": " + movieResponse.code());
            }
        }
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
                JsonNode cast = creditsDetails.get("cast");
                System.out.println("Actors:");
                for (JsonNode actor : cast) {
                    System.out.println(" - " + actor.get("name").asText() + " as " + actor.get("character").asText());
                }

                // Print crew information
                JsonNode crew = creditsDetails.get("crew");
                System.out.println("Crew:");
                for (JsonNode crewMember : crew) {
                    System.out.println(" - " + crewMember.get("name").asText() + " (" + crewMember.get("job").asText() + ")");
                }
            } else {
                System.out.println("Failed to fetch credits for movie ID " + movieId + ": " + creditsResponse.code());
            }
        }
    }
}
