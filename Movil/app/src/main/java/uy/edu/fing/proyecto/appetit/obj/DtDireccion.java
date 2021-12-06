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
public class DtDireccion {
    private Long id;
    private String alias;
    private String calle;
    private String numero;
    private String apartamento;
    private String referencias;
    private String geometry;
    //private String barrio;
}
