package dat.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
}
