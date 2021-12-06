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
public class DtPromocion {
    private Long id;
    private String nombre;
    private Long id_restaurante;
    private String nom_restaurante;
    private String descripcion;
    private Double descuento;
    private Double precio;
    private List<DtMenu> menus;
    private byte[] imagen;
    private Integer calificacion;
}
