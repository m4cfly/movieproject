package dat.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.DTO.ActorDTO;
import dat.DTO.DirectorDTO;
import dat.DTO.MovieDTO;
import dat.dao.JPAMovieDAO;
import dat.entities.Movie;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchTMDbData {

    static EntityManagerFactory entityManagerFactory = HibernateConfig.getEntityManagerFactory("moviedb");
    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    static String apiKey = System.getenv("STRING_API_KEY");

    public static void main(String[] args) {
        String filePath = "movies.json";
        List<Movie> movieList = readMoviesFromFile(filePath);

        if (movieList == null || movieList.isEmpty()) {
            movieList = new ArrayList<>();
            fetchMovieData(movieList);
        } else {
            System.out.println("Movies loaded from file: " + filePath);
        }

        saveMoviesToDatabase(movieList);
    }

    public static void fetchMovieData(List<Movie> movieList) {
        JPAMovieDAO jpaMovieDAO = new JPAMovieDAO(entityManagerFactory);
        OkHttpClient client = new OkHttpClient();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<MovieDTO> moviesList = new ArrayList<>();

        for (int page = 1; page <= 5; page++) {

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
                    JsonNode rootNode = objectMapper.readTree(jsonResponse);
                    JsonNode movies = rootNode.get("results");

                    for (JsonNode movieNode : movies) {
                        MovieDTO movieDTO = objectMapper.treeToValue(movieNode, MovieDTO.class);
                        movieDTO.setId(movieNode.get("id").asLong());
                        String movieId = movieNode.get("id").asText();

                        fetchMovieDetails(client, objectMapper, movieId, movieDTO);
                        fetchMovieCredits(client, objectMapper, movieId, movieDTO);

                        moviesList.add(movieDTO);
                        Movie movie = new Movie(movieDTO);
                        jpaMovieDAO.saveMovie(movie);
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.code() + " - " + response.message());
                } Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        JPAMovieDAO movieDAO = new JPAMovieDAO(entityManagerFactory);

        try {
            movieDAO.saveMovies(movieList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                movie.setAdult(movieDetails.has("adult") ? movieDetails.get("adult").asBoolean() : false);
                movie.setOverview(movieDetails.get("overview").asText());
                movie.setReleaseDate(movieDetails.get("release_date").asText());
                movie.setPopularity(movieDetails.has("popularity") ? movieDetails.get("popularity").asDouble() : 0.0);
            } else {
                System.out.println("Failed to fetch movie details for ID " + movieId + ": " + movieResponse.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

                List<ActorDTO> actorList = new ArrayList<>();
                JsonNode castArray = creditsDetails.get("cast");

                if (castArray != null && castArray.isArray()) {
                    for (JsonNode castNode : castArray) {
                        ActorDTO cast = objectMapper.treeToValue(castNode, ActorDTO.class);
                        String birthdate = fetchPersonDetails(client, String.valueOf(cast.getId()));
                        cast.setBirthDate(birthdate);
                        actorList.add(cast);
                    }
                }

                DirectorDTO directorDTO = null;
                JsonNode crewArray = creditsDetails.get("crew");

                if (crewArray != null && crewArray.isArray()) {
                    for (JsonNode crewNode : crewArray) {
                        if ("Director".equals(crewNode.get("job").asText())) {
                            directorDTO = objectMapper.treeToValue(crewNode, DirectorDTO.class);
                            String birthdate = fetchPersonDetails(client, String.valueOf(directorDTO.getId()));
                            directorDTO.setBirthDate(birthdate);
                            break;
                        }
                    }
                }

                movie.setActors(actorList);
                movie.setDirector(directorDTO);

            } else {
                System.out.println("Failed to fetch credits for movie ID " + movieId + ": " + creditsResponse.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                return personDetails.has("birthday") ? personDetails.get("birthday").asText() : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}