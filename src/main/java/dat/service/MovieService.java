package dat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.DTO.MovieDTO;
import dat.dao.MovieDAO;
import dat.dao.JSONMovieDAO;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
public class MovieService {

    private final MovieDAO movieDAO;

    public void fetchAndInsertMovies(String apiUrl) {
        try {
            String jsonResponse = fetchDataFromApi(apiUrl);
            List<MovieDTO> movies = parseJsonToMovies(jsonResponse);
            //movieDAO.saveMovies(movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchDataFromApi(String apiUrl) throws Exception {
        StringBuilder response = new StringBuilder();
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private List<MovieDTO> parseJsonToMovies(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDTO[] movieDTOS = objectMapper.readValue(jsonResponse, MovieDTO[].class);
        return List.of(movieDTOS);
    }

    public static void main(String[] args) {
        MovieDAO movieDAO = new JSONMovieDAO(new ObjectMapper());
        MovieService movieService = new MovieService(movieDAO);

        String apiUrl = "https://api.themoviedb.org/3/movie/tt0145487"; // Replace with your API URL
        movieService.fetchAndInsertMovies(apiUrl);
    }

}
