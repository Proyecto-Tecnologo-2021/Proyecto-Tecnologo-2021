package uy.edu.fing.proyecto.appetit.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtCotizacion {
    private String base;
    private Double sell;
    private Double buy;
}
