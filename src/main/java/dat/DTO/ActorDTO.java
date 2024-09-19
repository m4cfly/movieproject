package dat.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDTO {
    private boolean adult;
    private int gender;
    private long id;


    private String name;


    private double popularity;


    @JsonProperty("cast_id")
    private int actorId;

    private String character;

}
