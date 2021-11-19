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
public class DtReclamo {
    private String motivo;
    private String detalle;
}
