package dat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.DTO.ActorDTO;
import dat.DTO.DirectorDTO;
import dat.DTO.GenreDTO;
import dat.DTO.MovieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MovieService {


        ObjectMapper om = new ObjectMapper();
        private static final String FILE_PATH = "movieInfo.json";

        public static void main(String[] args) {
            dat.DTO.MovieDTO movie = new dat.DTO.MovieDTO();
            movie.readDTOsFromFile();
            // movie readMovieFromFile = new Movie();
        }
        public void readDTOsFromFile(){
            try {
                dat.DTO.MovieDTO[] movieDTOS = om.readValue(new File("movieInfo"), dat.DTO.MovieDTO[].class);
                for (dat.DTO.MovieDTO movieDTO : movieDTOS) {
                    System.out.println(movieDTO);
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    public void writeDTOsToFile(){
        List<MovieDTO> movies = Arrays.asList(
                new MovieDTO("sveske","djohn","1990-01-01",
                        new GenreDTO("Genrenavn","Beskrivelse"),
                        new ActorDTO("ActorNavn",LocalDate.of(2000,10,10),
                        new DirectorDTO("DirectorName",LocalDate.of(2000,10,10)));
        MovieDTO[] accountArray = movies.toArray(new MovieDTO[0]);
        for (MovieDTO accountDTO : accountArray) {
            System.out.println(accountDTO);
        }

        try {
            om
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), movies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class MovieDTO{
        private String title;
        private LocalDate releaseDate;

    }
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class GenreDTO{
        private String name;
        private String description;

    }
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class ActorDTO{
        private String name;
        private LocalDate birthdate;
    }
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class DirectorDTO{
            private String name;
            private LocalDate birthdate;
    }
}


