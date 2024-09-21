package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDTO {
    private int id;  // Actor ID

    private String birthDate;  // Actor's birthdate

    private String name;  // Actor's name

    private String job;  // Actor's job (e.g., "Actor")

    private String character;  // Name of the character the actor plays
}
