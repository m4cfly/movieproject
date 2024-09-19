package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MovieFileWriter {

    public static void writeMoviesToFile(List<Movie> movieList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert movie list to JSON and write to file
            objectMapper.writeValue(new File("movies.json"), movieList);
            System.out.println("Movies have been written to movies.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
