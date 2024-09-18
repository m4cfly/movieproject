package dat.DTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.entities.Movie;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MovieDTO {
    ObjectMapper om = new ObjectMapper();
    private static final String FILE_PATH = "movieInfo.json";

    public static void main(String[] args) {
        MovieDTO movie = new MovieDTO();
       // movie readMovieFromFile = new Movie();
    }
    public void readDTOsFromFile(){
        try {
            MovieDTO[] movieDTOS = om.readValue(new File("movieInfo"), MovieDTO[].class);
            for (MovieDTO movieDTO : movieDTOS) {
                System.out.println(movieDTO);
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }
//    public void writeDTOsToFile(){
//        List<MovieDTO> movies = Arrays.asList(
//                new AccountDTO("sveske","djohn","1990-01-01",
//                        new AddressDTO("street1","COPENHAGEN",1234),
//                        new AccountInfoDTO("2","100",true)));
//        AccountDTO[] accountArray = accounts.toArray(new AccountDTO[0]);
//        for (AccountDTO accountDTO : accountArray) {
//            System.out.println(accountDTO);
//        }
//
//        try {
//            om
//                    .writerWithDefaultPrettyPrinter()
//                    .writeValue(new File(FILE_PATH), accounts);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @Getter
//    @Setter
//    @Entity
//    private static class MovieDTO{
//        private String title;
//        private String releaseDate;
//
//    }
}
