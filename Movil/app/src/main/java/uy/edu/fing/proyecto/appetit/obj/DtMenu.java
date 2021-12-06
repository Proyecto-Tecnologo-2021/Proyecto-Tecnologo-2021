package uy.edu.fing.proyecto.appetit.obj;

import java.util.List;

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
public class DtMenu {
    private Long id; //ID DE MENU
    private Long id_restaurante;
    private String nom_restaurante;
    private Double descuento;
    private String nombre;
    private String descripcion;
    private Double precioSimple;
    private Double precioTotal;
    private List<DtExtraMenu> extras;
    private List<DtProducto> productos;
    private byte[] imagen;
    private Integer calificacion;
}
