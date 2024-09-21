package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectorDTO {
    private int id;  // Director ID

    private String birthDate;  // Director's birthdate

    private String name;  // Director's name

    private String job;  // Director's job (e.g., "Director")
}
