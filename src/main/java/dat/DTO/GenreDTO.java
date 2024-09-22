package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenreDTO {
    private long id;  // Genre ID

    private String name;  // Genre name (e.g., "Action", "Drama")
}
