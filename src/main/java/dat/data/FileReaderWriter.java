package dat.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import dat.entities.Movie;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileReaderWriter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
}
