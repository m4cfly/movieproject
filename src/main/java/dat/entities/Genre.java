package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.GenreDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@JsonIgnoreProperties (ignoreUnknown = true)
public class Genre {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String name;


    public Genre(GenreDTO genreDTO) {
        this.id = genreDTO.getId();
        this.name = genreDTO.getName();
    }

    public Genre(List<GenreDTO> genres) {
        for (int i = 0; i < genres.lastIndexOf(genres); i++) {
            this.id = genres.get(i).getId();
            this.name = genres.get(i).getName();
        }

    }
}
