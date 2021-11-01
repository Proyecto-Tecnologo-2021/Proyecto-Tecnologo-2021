package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionGralClienteDTO {

    private Integer clasificacion;
    private Integer restaurantes;

}
