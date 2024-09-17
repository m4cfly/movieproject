package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty ("id")
    private Long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private int birthdate;

    public Long getId() {
        return id;
    }
}
