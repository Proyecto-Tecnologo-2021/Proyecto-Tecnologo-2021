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
public class DtExtraMenu {
    private Long id;
    private Long id_producto;
    private Long id_restaurante;
    private String producto;
    private Double precio;
}
