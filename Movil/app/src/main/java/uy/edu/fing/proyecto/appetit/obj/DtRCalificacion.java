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
public class DtRCalificacion {
    private Integer rapidez;
    private Integer comida;
    private Integer servicio;
    private Integer general;

}
