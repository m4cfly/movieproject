package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.DTO.GenreDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "movies")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Genre {
    @Id
    @Column(name = "id", nullable = false)
    private long id;  // ID kommer fra TMDb API

    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies = new ArrayList<>();

    public Genre(GenreDTO genreDTO) {
        this.id = genreDTO.getId();
        this.name = genreDTO.getName();
    }

    public static List<Genre> fromDTOList(List<GenreDTO> genreDTOs) {
        List<Genre> genres = new ArrayList<>();
        for (GenreDTO dto : genreDTOs) {
            genres.add(new Genre(dto));
        }
        return genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return Objects.equals(getId(), genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}