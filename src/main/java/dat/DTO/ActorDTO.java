package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDTO {
    private int gender;
    private long id;

    @JsonProperty ("birth_date")
    private String birthDate;
    private String name;


    private double popularity;


    @JsonProperty("cast_id")
    private int actorId;

    private String character;

}
